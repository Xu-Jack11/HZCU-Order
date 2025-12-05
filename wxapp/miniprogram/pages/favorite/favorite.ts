// favorite.ts
// 我的收藏列表

Page({
  data: {
    favorites: [] as any[],
    emptyText: '还没有收藏的商家，去首页发现好店吧～'
  },

  onShow() {
    this.loadFavorites();
  },

  // 加载收藏列表
  loadFavorites() {
    const favorites = wx.getStorageSync('favoriteShops') || [];
    this.setData({ favorites });
  },

  // 取消收藏
  removeFavorite(e: any) {
    const shopId = e.currentTarget.dataset.id;
    const favorites = (wx.getStorageSync('favoriteShops') || []).filter((item: any) => item.id !== shopId);
    wx.setStorageSync('favoriteShops', favorites);
    this.setData({ favorites });
    wx.showToast({ title: '已取消收藏', icon: 'success' });
  },

  // 跳转店铺
  goToShop(e: any) {
    const shopId = e.currentTarget.dataset.id;
    if (!shopId) return;
    wx.navigateTo({ url: `/pages/shop/shop?id=${shopId}` });
  },

  goHome() {
    wx.switchTab({ url: '/pages/index/index' });
  },

  // 清空收藏
  clearAll() {
    wx.showModal({
      title: '提示',
      content: '确认清空全部收藏吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('favoriteShops');
          this.setData({ favorites: [] });
          wx.showToast({ title: '已清空', icon: 'success' });
        }
      }
    });
  }
});
