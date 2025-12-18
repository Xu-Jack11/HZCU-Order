// 直接使用绝对地址，避免路径前缀被误改导致 404/500
const ABS_BASE = 'http://localhost:8080/api/v1';

interface UserInfo {
  openid?: string;
  unionid?: string;
  nickName?: string;
  avatarUrl?: string;
  phone?: string;
  gender?: number;
  city?: string;
  province?: string;
  country?: string;
}

Page({
  data: {
    phone: '',
    canSubmit: false,
    code: '',
    showSecondaryLogin: false,
    showPhoneLogin: false,
    loginType: 'wechat' // 'wechat' | 'phone'
  },

  onLoad(options) {
    // 检查是否已登录
    const token = wx.getStorageSync('token');
    if (token) {
      wx.switchTab({
        url: '/pages/mine/mine'
      });
      return;
    }

    // 检查来源，如果是绑定手机号页面跳转过来的
    if (options.from === 'mine') {
      this.setData({
        showPhoneLogin: true,
        loginType: 'phone'
      });
    }
  },

  onPhoneInput(e: any) {
    const phone = e.detail.value || '';
    this.setData({ phone, canSubmit: /^1\d{10}$/.test(phone) });
  },

  // 获取微信手机号
  onGetPhoneNumber(e: any) {
    console.log('获取手机号结果:', e.detail);

    if (e.detail.errMsg === 'getPhoneNumber:ok') {
      // 获取手机号成功
      const code = e.detail.code;
      const encryptedData = e.detail.encryptedData;
      const iv = e.detail.iv;

      console.log('手机号code:', code);

      // 调用登录API，包含手机号
      this.doWechatLoginWithPhone(code, encryptedData, iv);
    } else if (e.detail.errMsg === 'getPhoneNumber:fail user deny') {
      // 用户拒绝授权
      console.log('用户拒绝授权手机号');
      this.setData({
        showSecondaryLogin: true,
        showPhoneLogin: true
      });
      wx.showToast({
        title: '需要手机号授权才能继续',
        icon: 'none',
        duration: 2000
      });
    } else {
      // 其他错误
      console.log('获取手机号失败:', e.detail.errMsg);
      this.setData({
        showSecondaryLogin: true,
        showPhoneLogin: true
      });
      wx.showToast({
        title: '获取手机号失败，请手动登录',
        icon: 'none'
      });
    }
  },

  // 带手机号的微信登录
  async doWechatLoginWithPhone(phoneCode: string, encryptedData?: string, iv?: string) {
    try {
      wx.showLoading({ title: '登录中...', mask: true });

      console.log('开始微信登录流程（含手机号）...');
      console.log('手机号code:', phoneCode);

      // 1. 获取微信登录凭证
      const loginRes = await this.wxLogin();
      console.log('获取到登录凭证:', loginRes);

      if (!loginRes.code) {
        throw new Error('获取微信登录凭证失败');
      }

      // 2. 获取用户信息
      const userProfile = await this.getUserProfile();
      console.log('获取到用户信息:', userProfile);

      // 3. 调用后端登录接口，包含手机号
      const url = `${ABS_BASE}/users/wechat-login`;
      console.log('调用后端接口（含手机号）:', url);

      const requestData: any = {
        code: loginRes.code,
        userInfo: userProfile,
        phoneCode: phoneCode
      };

      if (encryptedData && iv) {
        requestData.encryptedData = encryptedData;
        requestData.iv = iv;
      }

      const res = await this.request({
        url,
        method: 'POST',
        data: requestData
      });

      console.log('后端响应:', res);

      if (res.code === 0) {
        // 保存用户信息和token
        wx.setStorageSync('token', res.data.token);
        wx.setStorageSync('userInfo', res.data.user);
        wx.setStorageSync('openid', res.data.user.openid);

        console.log('登录成功，保存的用户信息:', res.data.user);

        wx.showToast({ title: '登录成功', icon: 'success' });
        setTimeout(() => {
          wx.switchTab({
            url: '/pages/mine/mine'
          });
        }, 1000);
      } else {
        throw new Error(res.message || '登录失败');
      }
    } catch (err: any) {
      console.error('微信登录失败:', err);
      wx.showToast({
        title: err.message || '微信登录失败',
        icon: 'none',
        duration: 3000
      });
    } finally {
      wx.hideLoading();
    }
  },

  // 微信登录（不获取手机号，备用方案）
  async doWechatLogin() {
    try {
      wx.showLoading({ title: '登录中...', mask: true });

      console.log('开始微信登录流程（备用方案）...');

      // 1. 获取微信登录凭证
      const loginRes = await this.wxLogin();
      console.log('获取到登录凭证:', loginRes);

      if (!loginRes.code) {
        throw new Error('获取微信登录凭证失败');
      }

      // 2. 获取用户信息
      const userProfile = await this.getUserProfile();
      console.log('获取到用户信息:', userProfile);

      // 3. 调用后端登录接口
      const url = `${ABS_BASE}/users/wechat-login`;
      console.log('调用后端接口:', url);

      const res = await this.request({
        url,
        method: 'POST',
        data: {
          code: loginRes.code,
          userInfo: userProfile
        }
      });

      console.log('后端响应:', res);

      if (res.code === 0) {
        // 保存用户信息和token
        wx.setStorageSync('token', res.data.token);
        wx.setStorageSync('userInfo', res.data.user);
        wx.setStorageSync('openid', res.data.user.openid);

        console.log('登录成功，保存的用户信息:', res.data.user);

        wx.showToast({ title: '登录成功', icon: 'success' });
        setTimeout(() => {
          wx.switchTab({
            url: '/pages/mine/mine'
          });
        }, 1000);
      } else {
        throw new Error(res.message || '登录失败');
      }
    } catch (err: any) {
      console.error('微信登录失败:', err);
      wx.showToast({
        title: err.message || '微信登录失败',
        icon: 'none',
        duration: 3000
      });
    } finally {
      wx.hideLoading();
    }
  },

  // 微信登录获取code
  wxLogin(): Promise<any> {
    return new Promise((resolve, reject) => {
      wx.login({
        success: resolve,
        fail: reject
      });
    });
  },

  // 获取用户信息
  async getUserProfile(): Promise<UserInfo> {
    return new Promise((resolve, reject) => {
      // 首先尝试使用新的 getUserProfile API
      if (wx.getUserProfile) {
        wx.getUserProfile({
          desc: '用于完善用户资料',
          success: (res) => {
            const userInfo: UserInfo = {
              nickName: res.userInfo.nickName,
              avatarUrl: res.userInfo.avatarUrl,
              gender: res.userInfo.gender,
              city: res.userInfo.city,
              province: res.userInfo.province,
              country: res.userInfo.country
            };
            resolve(userInfo);
          },
          fail: (err) => {
            console.warn('getUserProfile失败，尝试使用getUserInfo:', err);
            // 如果 getUserProfile 失败，尝试使用 getUserInfo
            this.getUserInfo().then(resolve).catch(reject);
          }
        });
      } else {
        // 如果没有 getUserProfile，使用 getUserInfo
        this.getUserInfo().then(resolve).catch(reject);
      }
    });
  },

  // 备用获取用户信息方法
  getUserInfo(): Promise<UserInfo> {
    return new Promise((resolve, reject) => {
      wx.getUserInfo({
        success: (res) => {
          const userInfo: UserInfo = {
            nickName: res.userInfo.nickName,
            avatarUrl: res.userInfo.avatarUrl,
            gender: res.userInfo.gender,
            city: res.userInfo.city,
            province: res.userInfo.province,
            country: res.userInfo.country
          };
          resolve(userInfo);
        },
        fail: (err) => {
          console.warn('getUserInfo失败，使用默认信息:', err);
          // 如果都失败，返回默认信息
          const defaultInfo: UserInfo = {
            nickName: '微信用户',
            avatarUrl: '',
            gender: 0,
            city: '',
            province: '',
            country: '中国'
          };
          resolve(defaultInfo);
        }
      });
    });
  },

  // 封装请求方法
  request(options: any): Promise<any> {
    return new Promise((resolve, reject) => {
      console.log('发起请求:', options);

      wx.request({
        ...options,
        header: {
          'Content-Type': 'application/json',
          ...options.header
        },
        success: (res) => {
          console.log('请求响应:', res);

          if (res.statusCode === 200) {
            resolve(res.data);
          } else {
            const error = new Error(`HTTP错误: ${res.statusCode}`);
            (error as any).statusCode = res.statusCode;
            (error as any).data = res.data;
            reject(error);
          }
        },
        fail: (err) => {
          console.error('请求失败:', err);
          reject(new Error(err.errMsg || '网络请求失败'));
        }
      });
    });
  },

  // 手机号登录（保留原有功能）
  async doLogin() {
    const phone = this.data.phone;
    try {
      wx.showLoading({ title: '登录中...', mask: true });

      const url = `${ABS_BASE}/users/login`;
      const res = await this.request({
        url,
        method: 'POST',
        data: { phone }
      });

      if (res.code === 0) {
        const user = res.data;
        const userInfo = wx.getStorageSync('userInfo') || {
          avatarUrl: '',
          nickName: '微信用户',
          phone: ''
        };
        const newInfo = { ...userInfo, phone: user.phone };

        wx.setStorageSync('userInfo', newInfo);
        if (user.token) {
          wx.setStorageSync('token', user.token);
        }

        wx.showToast({ title: '登录成功', icon: 'success' });
        setTimeout(() => {
          wx.navigateBack({ delta: 1 });
        }, 500);
      } else {
        throw new Error(res.message || '登录失败');
      }
    } catch (err: any) {
      console.error('手机号登录失败:', err);
      wx.showToast({
        title: err.message || '登录失败',
        icon: 'none'
      });
    } finally {
      wx.hideLoading();
    }
  }
});
