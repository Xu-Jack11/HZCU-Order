// mine.ts
// 个人中心页

Page({
  data: {
    isLogin: false,
    userInfo: {
      avatarUrl: '',
      nickName: '',
      phone: ''
    },
    orderCount: {
      pending: 2,
      preparing: 1,
      ready: 1
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
    // 这里应该调用API获取各状态订单数量
    // 目前使用模拟数据
  },

  // 登录
  goLogin() {
    wx.getUserProfile({
      desc: '用于完善用户信息',
      success: (res) => {
        const userInfo = {
          avatarUrl: res.userInfo.avatarUrl,
          nickName: res.userInfo.nickName,
          phone: ''
        };
        wx.setStorageSync('userInfo', userInfo);
        this.setData({
          isLogin: true,
          userInfo
        });
      },
      fail: () => {
        wx.showToast({
          title: '登录失败',
          icon: 'none'
        });
      }
    });
  },

  // 设置
  goSetting() {
    wx.navigateTo({
      url: '/pages/setting/setting'
    });
  },

  // 订单列表
  goOrderList(e: any) {
    const status = e.currentTarget.dataset.status;
    wx.navigateTo({
      url: `/pages/order/order?status=${status}`
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
    wx.navigateTo({
      url: '/pages/coupon/coupon'
    });
  },

  // 帮助中心
  goHelp() {
    wx.navigateTo({
      url: '/pages/help/help'
    });
  },

  // 意见反馈
  goFeedback() {
    wx.navigateTo({
      url: '/pages/feedback/feedback'
    });
  },

  // 关于我们
  goAbout() {
    wx.navigateTo({
      url: '/pages/about/about'
    });
  }
});
