// mine.ts
// 个人中心页
import { clearCouponContext } from '../../utils/coupon';
import { loginWithCode, logout, getUserProfile, rechargeBalance } from '../../utils/api';

Page({
  data: {
    isLogin: false,
    userInfo: {
      avatarUrl: '',
      nickName: '',
      mobile: '',
      balance: '0.00'
    },
    orderCount: {
      pending: 0,
      preparing: 0,
      ready: 0
    },
    couponCount: 0
  },

  onLoad() {
    this.checkLoginStatus();
  },

  onShow() {
    this.checkLoginStatus();
    if (this.data.isLogin) {
      this.loadOrderCount();
      this.refreshUserProfile();
    }
  },

  // 检查登录状态
  checkLoginStatus() {
    const userInfo = wx.getStorageSync('userInfo');
    const token = wx.getStorageSync('token');
    if (userInfo && token) {
      this.setData({
        isLogin: true,
        userInfo
      });
    } else {
      this.setData({ isLogin: false });
    }
  },

  // 刷新用户信息（获取最新余额等）
  async refreshUserProfile() {
    if (!this.data.isLogin) return;
    try {
      const profile = await getUserProfile();
      if (profile) {
        const userInfo = {
          ...this.data.userInfo,
          nickName: profile.nickname,
          avatarUrl: profile.avatarUrl,
          mobile: profile.mobile,
          balance: profile.balance ? profile.balance.toFixed(2) : '0.00'
        };
        this.setData({ userInfo });
        wx.setStorageSync('userInfo', userInfo);
      }
    } catch (err) {
      console.error('Failed to refresh user profile:', err);
    }
  },

  // 充值功能
  goRecharge() {
    wx.showModal({
      title: '余额充值',
      editable: true,
      placeholderText: '请输入充值金额',
      success: async (res) => {
        if (res.confirm && res.content) {
          const amount = parseFloat(res.content);
          if (isNaN(amount) || amount <= 0) {
            wx.showToast({ title: '请输入有效金额', icon: 'none' });
            return;
          }

          try {
            wx.showLoading({ title: '充值中...' });
            await rechargeBalance(amount);
            wx.hideLoading();
            wx.showToast({ title: '充值成功', icon: 'success' });
            this.refreshUserProfile();
          } catch (err) {
            wx.hideLoading();
            wx.showToast({ title: '充值失败', icon: 'none' });
            console.error('Recharge failed:', err);
          }
        }
      }
    });
  },

  // 加载订单数量
  loadOrderCount() {
    // Implement real API call if needed
  },

  // 登录入口：合并获取手机号
  onGetPhoneNumber(e: any) {
    console.log('onGetPhoneNumber event:', e);

    // Check if error is due to permission (common for personal accounts)
    if (e.detail.errMsg !== 'getPhoneNumber:ok') {
      console.error('getPhoneNumber failed:', e.detail.errMsg);

      if (e.detail.errMsg.includes('no permission') || e.detail.errMsg.includes('user deny')) {
        wx.showModal({
          title: '开发模式提示',
          content: '当前AppID无权限获取手机号（可能是个人账号）。是否使用模拟手机号登录？',
          success: (res) => {
            if (res.confirm) {
              this.handleLoginWithMockPhone();
            }
          }
        });
        return;
      }

      wx.showToast({ title: '授权失败', icon: 'none' });
      return;
    }

    const phoneCode = e.detail.code;
    console.log('phoneCode obtained:', phoneCode);
    this.performLogin(phoneCode);
  },

  handleLoginWithMockPhone() {
    console.log('Using mock phone number for login');
    // Using a special indicator or null for phoneCode, backend should handle or we use a separate mock flow
    // Ideally, we still need wx.login code
    this.performLogin(undefined, true);
  },

  performLogin(phoneCode?: string, isMock: boolean = false) {

    wx.login({
      success: async (loginRes) => {
        console.log('wx.login success, code:', loginRes.code);
        if (loginRes.code) {
          try {
            wx.showLoading({ title: '登录中...' });

            // If using mock phone, pass a special flag or handle differently.
            // For simplicity, if isMock is true, we pass undefined for phoneCode. 
            // NOTE: Backend needs to handle "login without phone code" or we need a dev endpoint.
            // But since 'loginWithCode' takes phoneCode, if we pass undefined, it just logs in without binding.

            const data = await loginWithCode(loginRes.code, undefined, undefined, phoneCode);
            console.log('Login success, data:', data);

            // If it was a mock login, we might want to manually set a fake mobile in specific dev scenarios
            // but for now, let's just let the user log in.
            if (isMock) {
              data.user.mobile = "13800000000 (Mock)";
              wx.showToast({ title: '模拟登录成功/未绑定真实手机' });
            } else {
              wx.showToast({ title: '登录成功' });
            }

            wx.setStorageSync('token', data.token);
            
            // 统一字段名并格式化余额
            const userInfo = {
              ...data.user,
              nickName: data.user.nickname, // 后端返回的是 nickname
              balance: data.user.balance ? data.user.balance.toFixed(2) : '0.00'
            };
            
            wx.setStorageSync('userInfo', userInfo);

            this.setData({
              isLogin: true,
              userInfo: userInfo
            });
            wx.hideLoading();
          } catch (err) {
            wx.hideLoading();
            wx.showToast({ title: '登录失败', icon: 'none' });
            console.error(err);
          }
        }
      }
    });
  },

  // 退出登录
  async onLogout() {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await logout();
          } catch (e) {
            console.error('Remote logout error', e);
          }
          wx.removeStorageSync('token');
          wx.removeStorageSync('userInfo');
          this.setData({
            isLogin: false,
            userInfo: {
              avatarUrl: '',
              nickName: '',
              mobile: '',
              balance: '0.00'
            }
          });
          wx.showToast({ title: '已退出' });
        }
      }
    });
  },

  async refreshUserInfo() {
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo) {
      this.setData({ userInfo });
    }
  },

  // 设置
  goSetting() {
    wx.openSetting({});
  },

  // 订单列表
  goOrderList(e: any) {
    const status = e.currentTarget.dataset.status;
    wx.setStorageSync('orderStatus', status);
    wx.switchTab({
      url: '/pages/order/order'
    });
  },

  // 我的收藏
  goFavorite() {
    wx.navigateTo({
      url: '/pages/favorite/favorite'
    });
  },

  // 优惠券
  goCoupon() {
    clearCouponContext();
    wx.navigateTo({
      url: '/pages/coupon/coupon'
    });
  },

  // 帮助中心
  goHelp() {
    wx.showModal({
      title: '帮助中心',
      content: '常见问题与客服指南将很快上线。',
      showCancel: false
    });
  },

  // 意见反馈
  goFeedback() {
    wx.showModal({
      title: '意见反馈',
      content: '请通过客服联系我们。',
      showCancel: false
    });
  },

  // 关于我们
  goAbout() {
    wx.showModal({
      title: '关于我们',
      content: '城院点餐专注于校园点餐体验。',
      showCancel: false
    });
  }
});
