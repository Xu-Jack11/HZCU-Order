// 直接使用绝对地址，避免路径前缀被误改导致 404/500
const ABS_BASE = 'http://localhost:8080/api/v1';

Page({
  data: {
    phone: '',
    canSubmit: false
  },
  onPhoneInput(e: any) {
    const phone = e.detail.value || '';
    this.setData({ phone, canSubmit: /^1\d{10}$/.test(phone) });
  },
  async doLogin() {
    const phone = this.data.phone;
    try {
      const url = `${ABS_BASE}/users/login`;
      wx.request({
        url,
        method: 'POST',
        header: { 'Content-Type': 'application/json' },
        data: { phone },
        success: (res) => {
          if (res.statusCode === 200 && res.data && res.data.code === 0) {
            const user = res.data.data;
            const userInfo = wx.getStorageSync('userInfo') || { avatarUrl: '', nickName: '微信用户', phone: '' };
            const newInfo = { ...userInfo, phone: user.phone };
            wx.setStorageSync('userInfo', newInfo);
            wx.showToast({ title: '登录成功', icon: 'success' });
            setTimeout(() => wx.navigateBack({ delta: 1 }), 500);
          } else {
            const msg = (res.data && res.data.message) || `登录失败(${res.statusCode})`;
            wx.showToast({ title: msg, icon: 'none' });
          }
        },
        fail: (err) => {
          wx.showToast({ title: `请求失败: ${url}`, icon: 'none' });
        }
      });
    } catch (err: any) {
      const msg = err?.data?.message || err?.errMsg || '网络错误';
      wx.showToast({ title: msg, icon: 'none' });
    }
  }
});
