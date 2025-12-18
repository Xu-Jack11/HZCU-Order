// order.ts
// 订单列表页
import { cancelOrder, confirmPickup, fetchOrders, payOrder } from '../../utils/api';

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

const PAGE_SIZE = 6;

Page({
  data: {
    activeStatus: 'all',
    loading: false,
    noMore: false,
    page: 1,
    orderList: [] as OrderItem[]
  },

  onLoad() {
    this.loadOrderList();
  },

  onShow() {
    const status = wx.getStorageSync('orderStatus');
    if (status) {
      this.setData({
        activeStatus: status,
        page: 1,
        noMore: false
      });
      wx.removeStorageSync('orderStatus');
    }
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
  async loadOrderList() {
    if (this.data.loading || (this.data.noMore && this.data.page > 1)) return;
    
    // 检查登录状态
    const token = wx.getStorageSync('token');
    if (!token) {
      console.log('[order] No token found, redirecting to login');
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      setTimeout(() => {
        wx.navigateTo({ url: '/pages/login/login' });
      }, 1500);
      return;
    }
    
    this.setData({ loading: true });
    console.log('[order] Loading orders with token:', token);

    try {
      const res: any = await fetchOrders({
        status: this.data.activeStatus,
        page: this.data.page,
        pageSize: PAGE_SIZE
      });
      console.log('[order] Fetch orders response:', res);
      
      // 检查是否有错误响应
      if (res?.code && res.code !== '0' && res.code !== 0) {
        console.error('[order] API error:', res);
        if (res.code === '401') {
          wx.showToast({ title: '登录已过期，请重新登录', icon: 'none' });
          wx.removeStorageSync('token');
          setTimeout(() => {
            wx.navigateTo({ url: '/pages/login/login' });
          }, 1500);
          return;
        }
        throw new Error(res.message || '加载失败');
      }
      
      // 统一过滤掉已取消订单（兼容不同后端命名/大小写/文案）
      const list = (res?.list || []).filter((item: any) => {
        const status = String(item.status || '').toLowerCase();
        const text = String(item.statusText || '');
        const isCanceled = status === 'canceled' || status === 'cancelled' || /取消/.test(text);
        return !isCanceled;
      });
      const merged =
        this.data.page === 1 ? list : [...this.data.orderList, ...list];
      const total = res?.total || merged.length;
      const noMore = merged.length >= total || list.length < PAGE_SIZE;
      this.setData({
        loading: false,
        noMore,
        orderList: merged
      });
    } catch (error: any) {
      console.error('[order] Load orders error:', error);
      this.setData({ loading: false });
      wx.showToast({
        title: error?.message || '订单加载失败',
        icon: 'none'
      });
    }
  },

  // 加载更多
  loadMore() {
    if (this.data.loading || this.data.noMore) return;
    this.setData(
      {
        page: this.data.page + 1
      },
      () => this.loadOrderList()
    );
  },

  // 跳转订单详情
  goOrderDetail(e: any) {
    const orderId = Number(e.currentTarget.dataset.id);
    const order = this.data.orderList.find((item) => item.id === orderId);
    if (!order) return;
    wx.showModal({
      title: '订单详情',
      content: `商家：${order.shopName}\n金额：¥${order.totalPrice}\n状态：${order.statusText}`,
      showCancel: false
    });
  },

  // 取消订单
  cancelOrder(e: any) {
    const orderId = Number(e.currentTarget.dataset.id);
    wx.showModal({
      title: '提示',
      content: '确定要取消这个订单吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await cancelOrder(orderId);
            wx.showToast({
              title: '订单已取消',
              icon: 'success'
            });
            // 立即从当前列表中移除该订单，确保界面即时消失；并标记为已取消
            const updated = this.data.orderList.map((item) =>
              item.id === orderId ? { ...item, status: 'canceled', statusText: '已取消' } : item
            );
            const filtered = updated.filter((item) => item.id !== orderId);
            this.setData({ orderList: filtered });
            // 同步刷新列表以防分页/统计需要服务端最新数据
            this.refreshList();
          } catch (error) {
            wx.showToast({
              title: '取消失败',
              icon: 'none'
            });
          }
        }
      }
    });
  },

  // 去付款
  async payOrder(e: any) {
    const orderId = Number(e.currentTarget.dataset.id);
    try {
      await payOrder(orderId);
      wx.showToast({
        title: '已模拟支付',
        icon: 'success'
      });
      // 本地状态迁移：pending -> preparing；如果当前tab是待付款，则移除该订单
      const moved = this.data.orderList.map((item) =>
        item.id === orderId ? { ...item, status: 'preparing', statusText: '制作中' } : item
      );
      const shouldRemove = this.data.activeStatus === 'pending';
      const nextList = shouldRemove ? moved.filter((item) => item.id !== orderId) : moved;
      this.setData({ orderList: nextList });
      // 同步刷新服务端数据，确保分页/总数正确
      this.refreshList();
    } catch (error) {
      wx.showToast({
        title: '支付失败',
        icon: 'none'
      });
    }
  },

  // 再来一单
  reOrder(e: any) {
    const orderId = Number(e.currentTarget.dataset.id);
    const order = this.data.orderList.find((o) => o.id === orderId);
    if (order) {
      wx.navigateTo({
        url: `/pages/shop/shop?id=${order.shopId}`
      });
    }
  },

  // 去评价
  commentOrder() {
    wx.showToast({
      title: '评价功能即将上线',
      icon: 'none'
    });
  },

  // 确认取餐
  confirmPickup(e: any) {
    const orderId = Number(e.currentTarget.dataset.id);
    wx.showModal({
      title: '提示',
      content: '确认已取餐吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await confirmPickup(orderId);
            wx.showToast({
              title: '已确认取餐',
              icon: 'success'
            });
            // 本地状态迁移：ready -> completed；如果当前tab是待取餐，则移除该订单
            const moved = this.data.orderList.map((item) =>
              item.id === orderId ? { ...item, status: 'completed', statusText: '已完成' } : item
            );
            const shouldRemove = this.data.activeStatus === 'ready';
            const nextList = shouldRemove ? moved.filter((item) => item.id !== orderId) : moved;
            this.setData({ orderList: nextList });
            // 同步刷新服务端数据
            this.refreshList();
          } catch (error) {
            wx.showToast({
              title: '操作失败',
              icon: 'none'
            });
          }
        }
      }
    });
  },

  refreshList() {
    this.setData({
      page: 1,
      noMore: false
    });
    this.loadOrderList();
  },

  // 去首页点餐
  goHome() {
    wx.switchTab({
      url: '/pages/index/index'
    });
  }
});
