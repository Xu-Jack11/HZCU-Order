import { ShopItem } from '../../utils/data';
import { getFavoriteShops, removeFavoriteShop } from '../../utils/favorite';

Page({
  data: {
    favorites: [] as ShopItem[]
  },

  onShow() {
    this.loadFavorites();
  },

  loadFavorites() {
    const favorites = getFavoriteShops();
    this.setData({ favorites });
  },

  goToShop(e: any) {
    const shopId = Number(e.currentTarget.dataset.id);
    wx.navigateTo({
      url: `/pages/shop/shop?id=${shopId}`
    });
  },

  removeFavorite(e: any) {
    const shopId = Number(e.currentTarget.dataset.id);
    const favorites = removeFavoriteShop(shopId);
    this.setData({ favorites });
    wx.showToast({
      title: '已取消收藏',
      icon: 'none'
    });
  },

  goHome() {
    wx.switchTab({
      url: '/pages/index/index'
    });
  }
});
