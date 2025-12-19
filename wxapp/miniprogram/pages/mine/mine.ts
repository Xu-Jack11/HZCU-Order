// mine.ts
// 个人中心页
import { clearCouponContext } from '../../utils/coupon';
import { loginWithCode } from '../../utils/api';

Page({
  data: {
    isLogin: false,
    userInfo: {
      avatarUrl: '',
      nickName: '',
      phone: ''
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

  // 加载订单数量
  loadOrderCount() {
    // Implement real API call if needed
    // For now keep it static or zero
  },

  // 登录
  goLogin() {
    wx.getUserProfile({
      desc: '用于完善用户信息',
      success: (res) => {
        const profile = res.userInfo;
        wx.login({
          success: async (loginRes) => {
            if (loginRes.code) {
              try {
                wx.showLoading({ title: '登录中...' });
                const data = await loginWithCode(loginRes.code, profile.nickName, profile.avatarUrl);

                wx.setStorageSync('token', data.token);
                wx.setStorageSync('userInfo', data.user || {
                  nickName: profile.nickName,
                  avatarUrl: profile.avatarUrl
                });

                this.setData({
                  isLogin: true,
                  userInfo: wx.getStorageSync('userInfo')
                });
                wx.hideLoading();
                wx.showToast({ title: '登录成功' });
              } catch (err) {
                wx.hideLoading();
                wx.showToast({ title: '登录失败', icon: 'none' });
                console.error(err);
              }
            }
          }
        });
      },
      fail: () => {
        wx.showToast({
          title: '已取消授权',
          icon: 'none'
        });
      }
    });
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
      content: '常见问题与客服指南将很快上线，当前可直接联系客服或提交反馈。',
      showCancel: false
    });
  },

  // 意见反馈
  goFeedback() {
    wx.showModal({
      title: '意见反馈',
      content: '请通过客服或邮件 support@example.com 联系我们，您的建议会让产品更好。',
      showCancel: false
    });
  },

  // 关于我们
  goAbout() {
    wx.showModal({
      title: '关于我们',
      content: '城院点餐由校园信息化小组维护，致力于提供更快更稳的点餐体验。',
      showCancel: false
    });
  }
});
