// mine.ts
// 个人中心页
import { clearCouponContext } from '../../utils/coupon';

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
    couponCount: 3
  },

  onLoad() {
    this.checkLoginStatus();
  },

  onShow() {
    this.checkLoginStatus();
    this.loadOrderCount();
  },

  // 检查登录状态
  checkLoginStatus() {
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo) {
      this.setData({
        isLogin: true,
        userInfo
      });
    }
  },

  // 加载订单数量
  loadOrderCount() {
    // 依据后端分页返回的 total 统计各状态数量
    const fetchOrders = (status: string) => new Promise<number>((resolve) => {
      wx.request({
        url: 'http://localhost:8080/api/v1/orders',
        method: 'GET',
        data: { status, page: 1, pageSize: 1 },
        success: (res) => {
          const total = (res?.data?.data?.total) ?? 0;
          resolve(Number(total) || 0);
        },
        fail: () => resolve(0)
      });
    });

    Promise.all([
      fetchOrders('pending'),
      fetchOrders('preparing'),
      fetchOrders('ready')
    ]).then(([pending, preparing, ready]) => {
      this.setData({
        orderCount: { pending, preparing, ready }
      });
    });
  },

  // 跳转登录页面
  goLogin() {
    wx.navigateTo({ url: '/pages/login/login' });
  },

  // 退出登录
  logout() {
    wx.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          // 清除本地存储
          wx.removeStorageSync('token');
          wx.removeStorageSync('userInfo');
          wx.removeStorageSync('openid');

          // 更新页面状态
          this.setData({
            isLogin: false,
            userInfo: {
              avatarUrl: '',
              nickName: '',
              phone: ''
            }
          });

          wx.showToast({
            title: '已退出登录',
            icon: 'success'
          });
        }
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
    wx.showToast({
      title: '123',
      icon: 'none',
      duration: 1000
    });
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
