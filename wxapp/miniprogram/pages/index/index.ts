// index.ts
// 首页 - 商家列表
import { fetchCanteens } from '../../utils/api';
import { ShopItem } from '../../utils/data';
import { getFavoriteShops, toggleFavoriteShop } from '../../utils/favorite';

const PAGE_SIZE = 6;

Page({
  data: {
    location: '浙大城市学院（南校区）',
    searchKeyword: '',
    activeTab: 'nearby',
    selectedCategoryId: 0,
    loading: false,
    noMore: false,
    page: 1,
    favoriteShopIds: [] as number[],
    categories: [
      { id: 1, name: '汉堡披萨', icon: '/images/category/burger.png' },
      { id: 2, name: '炸鸡薯条', icon: '/images/category/chicken.png' },
      { id: 3, name: '水果', icon: '/images/category/fruit.png' },
      { id: 4, name: '甜品饮料', icon: '/images/category/drink.png' },
      { id: 5, name: '中餐', icon: '/images/category/chinese.png' },
      { id: 6, name: '面食', icon: '/images/category/noodle.png' }
    ],
    shopList: [] as (ShopItem & { isFavorite?: boolean })[]
  },

  onLoad() {
    this.syncFavoriteIds();
    this.loadShopList(true);
  },

  onShow() {
    this.syncFavoriteIds();
    this.loadShopList(true);
  },

  syncFavoriteIds() {
    const favoriteIds = getFavoriteShops().map((item) => item.id);
    this.setData({ favoriteShopIds: favoriteIds });
  },

  // 选择地址
  chooseLocation() {
    wx.chooseLocation({
      success: (res) => {
        this.setData({
          location: res.name || res.address
        });
      }
    });
  },

  // 搜索输入
  onSearchInput(e: any) {
    this.setData({
      searchKeyword: e.detail.value
    });
  },

  // 搜索
  onSearch() {
    this.loadShopList(true);
  },

  // 分类点击
  onCategoryTap(e: any) {
    const categoryId = Number(e.currentTarget.dataset.id);
    this.setData(
      {
        selectedCategoryId: categoryId,
        page: 1,
        noMore: false,
        shopList: []
      },
      () => this.loadShopList(true)
    );
  },

  // 筛选标签切换
  onTabChange(e: any) {
    const tab = e.currentTarget.dataset.tab;
    this.setData(
      {
        activeTab: tab,
        page: 1,
        shopList: [],
        noMore: false
      },
      () => this.loadShopList(true)
    );
  },

  // 加载商家列表
  async loadShopList(reset: boolean = false) {
    if (this.data.loading || (this.data.noMore && !reset)) return;
    const nextPage = reset ? 1 : this.data.page;
    this.setData({ loading: true, page: nextPage });

    try {
      const res = await fetchCanteens({
        page: nextPage,
        pageSize: PAGE_SIZE,
        keyword: (this.data.searchKeyword || '').trim(),
        categoryId: this.data.selectedCategoryId,
        sort: this.data.activeTab
      });
      const list = (res as any).list || [];
      const merged = nextPage === 1 ? list : [...this.data.shopList, ...list];
      const total = (res as any).total || merged.length;
      const noMore = merged.length >= total || list.length < PAGE_SIZE;
      this.setData({
        loading: false,
        noMore,
        shopList: this.decorateFavoriteState(merged)
      });
    } catch (error) {
      this.setData({ loading: false });
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
    }
  },

  decorateFavoriteState(list: ShopItem[]) {
    const favoriteIds = new Set(this.data.favoriteShopIds);
    return list.map((item) => ({
      ...item,
      isFavorite: favoriteIds.has(item.id)
    }));
  },

  // 加载更多
  loadMore() {
    if (this.data.loading || this.data.noMore) return;
    this.setData(
      {
        page: this.data.page + 1
      },
      () => this.loadShopList()
    );
  },

  // 收藏/取消收藏
  toggleFavorite(e: any) {
    const shopId = Number(e.currentTarget.dataset.id);
    const shop = this.data.shopList.find((item) => item.id === shopId);
    if (!shop) return;
    const nextFavorites = toggleFavoriteShop(shop);
    const isAdded = nextFavorites.some((item) => item.id === shopId);
    this.setData({
      favoriteShopIds: nextFavorites.map((item) => item.id),
      shopList: this.decorateFavoriteState(this.data.shopList)
    });
    wx.showToast({
      title: isAdded ? '已收藏' : '已取消收藏',
      icon: 'none'
    });
  },

  // 跳转到商家详情
  goToShop(e: any) {
    const shopId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/shop/shop?id=${shopId}`
    });
  }
});
