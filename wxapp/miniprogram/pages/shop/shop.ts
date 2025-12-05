// shop.ts
// 商家详情页
import { getCartSnapshot, saveCartSnapshot, clearCartSnapshot } from '../../utils/cart';
import { getShopById, ShopItem } from '../../utils/data';
import { isFavoriteShop, toggleFavoriteShop } from '../../utils/favorite';

interface GoodsItem {
  id: number;
  name: string;
  description: string;
  image: string;
  price: number;
  originalPrice?: number;
  monthlySales: number;
  goodRate: number;
  tags: string[];
  count: number;
}

interface Category {
  id: number;
  name: string;
  goods: GoodsItem[];
}

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
    shopInfo: {
      id: 1,
      name: '肯德基（城院店）',
      logo: '/images/shops/kfc.png',
      waitTime: 15,
      monthlySales: 719,
      notice: '共赏元月一轮，喜迎中秋良宵。',
      address: '浙江省杭州市拱墅区湖州街51号',
      businessHours: '09:00-22:00',
      phone: '0571-88888888',
      minPrice: 0,
      rating: 4.8,
      distance: '1.8km'
    },
    categories: [
      {
        id: 1,
        name: '热销',
        goods: [
          {
            id: 101,
            name: '嫩牛五方超值单人餐',
            description: '热卖品类优质商品',
            image: '/images/goods/niuwufang.png',
            price: 19.5,
            originalPrice: 19.7,
            monthlySales: 283,
            goodRate: 97,
            tags: ['9.9折'],
            count: 0
          },
          {
            id: 102,
            name: '香辣鸡腿堡单人餐',
            description: '经典人气套餐',
            image: '/images/goods/jileitui.png',
            price: 25.9,
            originalPrice: 28.0,
            monthlySales: 456,
            goodRate: 95,
            tags: ['热销'],
            count: 0
          }
        ]
      },
      {
        id: 2,
        name: '优惠',
        goods: [
          {
            id: 201,
            name: '超值午餐套餐',
            description: '限时特惠',
            image: '/images/goods/lunch.png',
            price: 15.9,
            originalPrice: 22.0,
            monthlySales: 189,
            goodRate: 94,
            tags: ['限时'],
            count: 0
          }
        ]
      },
      {
        id: 3,
        name: '单人套餐',
        goods: [
          {
            id: 301,
            name: '奥尔良烤鸡腿堡套餐',
            description: '含中薯+中可乐',
            image: '/images/goods/orleans.png',
            price: 32.0,
            monthlySales: 312,
            goodRate: 96,
            tags: [],
            count: 0
          }
        ]
      },
      {
        id: 4,
        name: '套餐',
        goods: [
          {
            id: 401,
            name: '双人欢享套餐',
            description: '2个汉堡+2份薯条+2杯可乐',
            image: '/images/goods/double.png',
            price: 59.0,
            originalPrice: 68.0,
            monthlySales: 98,
            goodRate: 98,
            tags: ['人气'],
            count: 0
          }
        ]
      }
    ] as Category[],
    comments: [
      {
        id: 1,
        avatar: '/images/avatar/user1.png',
        nickname: '美食家小王',
        time: '2024-01-15',
        rating: 5,
        content: '味道很好，出餐也很快，推荐！'
      },
      {
        id: 2,
        avatar: '/images/avatar/user2.png',
        nickname: '吃货达人',
        time: '2024-01-14',
        rating: 4,
        content: '分量足，性价比高'
      }
    ]
  },

  onLoad(options: any) {
    const shopId = options.id ? parseInt(options.id) : 1;
    this.setData({ shopId });
    this.loadShopInfo(shopId);
    this.restoreCartFromStorage(shopId);
  },

  onShow() {
    this.setData({
      isFavorite: isFavoriteShop(this.data.shopId)
    });
  },

  // 加载商家信息
  loadShopInfo(shopId: number) {
    const shop = getShopById(shopId);
    if (shop) {
      this.setData({
        shopInfo: { ...this.data.shopInfo, ...shop },
        isFavorite: isFavoriteShop(shop.id)
      });
    }
  },

  restoreCartFromStorage(shopId: number) {
    const snapshot = getCartSnapshot(shopId);
    if (!snapshot || !snapshot.cartList) return;
    const cartMap = new Map<number, number>();
    snapshot.cartList.forEach((item) => {
      cartMap.set(item.id, item.count);
    });
    const categories = this.data.categories;
    let totalPrice = 0;
    let totalCount = 0;
    const cartList: GoodsItem[] = [];

    categories.forEach((category) => {
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
      categories,
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
    const shopDetail: ShopItem | null = getShopById(this.data.shopId);
    if (!shopDetail) return;
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
