// shop.ts
// 商家详情页
import { fetchCanteenDetail } from '../../utils/api';
import { getCartSnapshot, saveCartSnapshot, clearCartSnapshot } from '../../utils/cart';
import { ShopItem } from '../../utils/data';
import { isFavoriteShop, toggleFavoriteShop } from '../../utils/favorite';
import { Category, GoodsItem } from '../../utils/mock';

Page({
  data: {
    shopId: 0,
    currentTab: 'menu',
    activeCategory: 0,
    scrollToView: '',
    showCartDetail: false,
    totalPrice: 0,
    totalCount: 0,
    canCheckout: false,
    cartList: [] as GoodsItem[],
    isFavorite: false,
    shopInfo: {} as ShopItem,
    categories: [] as Category[],
    comments: [] as any[]
  },

  onLoad(options: any) {
    const shopId = options.id ? parseInt(options.id) : 1;
    this.setData({ shopId });
    this.loadShopInfo(shopId);
  },

  onShow() {
    this.setData({
      isFavorite: isFavoriteShop(this.data.shopId)
    });
  },

  // 加载商家信息
  async loadShopInfo(shopId: number) {
    try {
      const res = await fetchCanteenDetail(shopId);
      this.setData(
        {
          // 仅使用后端数据，不再混入内存 mock
          shopInfo: res.shopInfo,
          isFavorite: isFavoriteShop(shopId),
          categories: res.categories || [],
          comments: res.comments || []
        },
        () => {
          this.restoreCartFromStorage(shopId, res.categories || []);
        }
      );
    } catch (error) {
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
      this.restoreCartFromStorage(shopId, this.data.categories);
    }
  },

  restoreCartFromStorage(shopId: number, categories?: Category[]) {
    const snapshot = getCartSnapshot(shopId);
    if (!snapshot || !snapshot.cartList) {
      if (categories && categories.length > 0) {
        this.setData({ categories });
      }
      return;
    }
    const cartMap = new Map<number, number>();
    snapshot.cartList.forEach((item) => {
      cartMap.set(item.id, item.count);
    });
    const nextCategories = categories && categories.length > 0 ? categories : this.data.categories;
    let totalPrice = 0;
    let totalCount = 0;
    const cartList: GoodsItem[] = [];

    nextCategories.forEach((category) => {
      category.goods.forEach((goods) => {
        const count = cartMap.get(goods.id) || 0;
        goods.count = count;
        if (count > 0) {
          totalPrice += goods.price * count;
          totalCount += count;
          cartList.push({ ...goods });
        }
      });
    });

    this.setData({
      categories: nextCategories,
      totalPrice: Math.round(totalPrice * 10) / 10,
      totalCount,
      cartList,
      canCheckout: totalCount > 0
    });
  },

  // 切换标签页
  switchTab(e: any) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({ currentTab: tab });
  },

  // 选择分类
  selectCategory(e: any) {
    const index = e.currentTarget.dataset.index;
    this.setData({
      activeCategory: index,
      scrollToView: `category-${index}`
    });
  },

  // 增加商品
  increaseGoods(e: any) {
    const goodsId = e.currentTarget.dataset.id;
    this.updateGoodsCount(goodsId, 1);
  },

  // 减少商品
  decreaseGoods(e: any) {
    const goodsId = e.currentTarget.dataset.id;
    this.updateGoodsCount(goodsId, -1);
  },

  // 更新商品数量
  updateGoodsCount(goodsId: number, delta: number) {
    const categories = this.data.categories;
    let totalPrice = 0;
    let totalCount = 0;
    const cartList: GoodsItem[] = [];

    categories.forEach((category) => {
      category.goods.forEach((goods) => {
        if (goods.id === goodsId) {
          goods.count = Math.max(0, goods.count + delta);
        }
        if (goods.count > 0) {
          totalPrice += goods.price * goods.count;
          totalCount += goods.count;
          cartList.push({ ...goods });
        }
      });
    });

    this.setData({
      categories,
      totalPrice: Math.round(totalPrice * 10) / 10,
      totalCount,
      cartList,
      canCheckout: totalCount > 0
    });

    saveCartSnapshot({
      shopId: this.data.shopId,
      shopInfo: this.data.shopInfo,
      cartList,
      totalPrice: Math.round(totalPrice * 10) / 10,
      totalCount
    });
  },

  // 显示购物车详情
  showCartDetail() {
    if (this.data.totalCount > 0) {
      this.setData({ showCartDetail: true });
    }
  },

  // 隐藏购物车详情
  hideCartDetail() {
    this.setData({ showCartDetail: false });
  },

  // 清空购物车
  clearCart() {
    const categories = this.data.categories;
    categories.forEach((category) => {
      category.goods.forEach((goods) => {
        goods.count = 0;
      });
    });

    this.setData({
      categories,
      totalPrice: 0,
      totalCount: 0,
      cartList: [],
      canCheckout: false,
      showCartDetail: false
    });
    clearCartSnapshot(this.data.shopId);
  },

  // 收藏/取消收藏
  toggleFavorite() {
    const shopDetail: ShopItem | undefined = this.data.shopInfo as ShopItem;
    if (!shopDetail || !shopDetail.id) return;
    const favorites = toggleFavoriteShop(shopDetail);
    const isFavorite = favorites.some((item) => item.id === shopDetail.id);
    this.setData({ isFavorite });
    wx.showToast({
      title: isFavorite ? '已收藏' : '已取消收藏',
      icon: 'none'
    });
  },

  // 显示警告信息
  showWarning() {
    wx.showModal({
      title: '温馨提示',
      content: '如遇食品质量问题，请及时联系客服',
      showCancel: false
    });
  },

  // 去结算
  goCheckout() {
    if (!this.data.canCheckout) {
      wx.showToast({
        title: '请先选择商品',
        icon: 'none'
      });
      return;
    }
    
    // 将购物车数据存储到本地
    wx.setStorageSync('cartData', {
      shopInfo: this.data.shopInfo,
      cartList: this.data.cartList,
      totalPrice: this.data.totalPrice,
      totalCount: this.data.totalCount
    });

    wx.navigateTo({
      url: '/pages/checkout/checkout'
    });
  }
});
