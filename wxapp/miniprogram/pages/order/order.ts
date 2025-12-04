// order.ts
// 订单列表页

interface GoodsItem {
  id: number;
  name: string;
  image: string;
  price: number;
  count: number;
}

interface OrderItem {
  id: number;
  shopId: number;
  shopName: string;
  shopLogo: string;
  goods: GoodsItem[];
  totalCount: number;
  totalPrice: number;
  status: string;
  statusText: string;
  createTime: string;
}

Page({
  data: {
    activeStatus: 'all',
    loading: false,
    noMore: false,
    page: 1,
    orderList: [
      {
        id: 1,
        shopId: 1,
        shopName: '肯德基（城院店）',
        shopLogo: '/images/shops/kfc.png',
        goods: [
          {
            id: 101,
            name: '嫩牛五方超值单人餐',
            image: '/images/goods/niuwufang.png',
            price: 19.5,
            count: 1
          },
          {
            id: 102,
            name: '香辣鸡腿堡单人餐',
            image: '/images/goods/jileitui.png',
            price: 25.9,
            count: 1
          }
        ],
        totalCount: 2,
        totalPrice: 45.4,
        status: 'completed',
        statusText: '已完成',
        createTime: '2024-01-15 12:30:00'
      },
      {
        id: 2,
        shopId: 2,
        shopName: '兰州拉面',
        shopLogo: '/images/shops/lamian.png',
        goods: [
          {
            id: 201,
            name: '兰州牛肉拉面',
            image: '/images/goods/lamian.png',
            price: 15,
            count: 2
          }
        ],
        totalCount: 2,
        totalPrice: 30,
        status: 'preparing',
        statusText: '制作中',
        createTime: '2024-01-15 11:00:00'
      },
      {
        id: 3,
        shopId: 3,
        shopName: '库迪咖啡（城院南校区店）',
        shopLogo: '/images/shops/cotti.png',
        goods: [
          {
            id: 301,
            name: '美式咖啡',
            image: '/images/goods/coffee.png',
            price: 9.9,
            count: 1
          }
        ],
        totalCount: 1,
        totalPrice: 9.9,
        status: 'pending',
        statusText: '待付款',
        createTime: '2024-01-15 10:30:00'
      }
    ] as OrderItem[]
  },

  onLoad() {
    this.loadOrderList();
  },

  onShow() {
    // 页面显示时刷新订单列表
    this.loadOrderList();
  },

  onPullDownRefresh() {
    this.setData({
      page: 1,
      noMore: false
    });
    this.loadOrderList();
    wx.stopPullDownRefresh();
  },

  // 切换订单状态
  switchStatus(e: any) {
    const status = e.currentTarget.dataset.status;
    this.setData({
      activeStatus: status,
      page: 1,
      noMore: false
    });
    this.loadOrderList();
  },

  // 加载订单列表
  loadOrderList() {
    if (this.data.loading) return;
    
    this.setData({ loading: true });
    
    // 模拟API请求
    setTimeout(() => {
      // 这里应该根据 activeStatus 筛选订单
      this.setData({
        loading: false
      });
    }, 500);
  },

  // 加载更多
  loadMore() {
    if (this.data.loading || this.data.noMore) return;
    this.setData({
      page: this.data.page + 1
    });
    this.loadOrderList();
  },

  // 跳转订单详情
  goOrderDetail(e: any) {
    const orderId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/order-detail/order-detail?id=${orderId}`
    });
  },

  // 取消订单
  cancelOrder(e: any) {
    const orderId = e.currentTarget.dataset.id;
    wx.showModal({
      title: '提示',
      content: '确定要取消这个订单吗？',
      success: (res) => {
        if (res.confirm) {
          // 调用取消订单API
          wx.showToast({
            title: '订单已取消',
            icon: 'success'
          });
          this.loadOrderList();
        }
      }
    });
  },

  // 去付款
  payOrder(e: any) {
    const orderId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/payment/payment?id=${orderId}`
    });
  },

  // 再来一单
  reOrder(e: any) {
    const orderId = e.currentTarget.dataset.id;
    // 根据订单ID找到对应订单
    const order = this.data.orderList.find(o => o.id === orderId);
    if (order) {
      wx.navigateTo({
        url: `/pages/shop/shop?id=${order.shopId}`
      });
    }
  },

  // 去评价
  commentOrder(e: any) {
    const orderId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/comment/comment?id=${orderId}`
    });
  },

  // 确认取餐
  confirmPickup(e: any) {
    const orderId = e.currentTarget.dataset.id;
    wx.showModal({
      title: '提示',
      content: '确认已取餐吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showToast({
            title: '已确认取餐',
            icon: 'success'
          });
          this.loadOrderList();
        }
      }
    });
  },

  // 去首页点餐
  goHome() {
    wx.switchTab({
      url: '/pages/index/index'
    });
  }
});
