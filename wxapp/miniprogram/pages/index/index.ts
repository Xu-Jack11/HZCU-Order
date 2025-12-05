// index.ts
// 首页 - 商家列表
import { shopCatalog, ShopItem } from '../../utils/data';
import { getFavoriteShops, toggleFavoriteShop } from '../../utils/favorite';

const PAGE_SIZE = 4;

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

  filteredShops: shopCatalog as ShopItem[],

  onLoad() {
    this.syncFavoriteIds();
    this.applyFilters();
  },

  onShow() {
    this.syncFavoriteIds();
    this.applyFilters(false);
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
    this.applyFilters();
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
      () => {
        this.applyFilters();
      }
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
      () => this.applyFilters()
    );
  },

  applyFilters(resetPage: boolean = true) {
    const keyword = (this.data.searchKeyword || '').trim();
    const categoryId = this.data.selectedCategoryId;

    let list = shopCatalog.slice();
    if (categoryId) {
      list = list.filter((shop) => shop.categoryIds.includes(categoryId));
    }
    if (keyword) {
      list = list.filter(
        (shop) =>
          shop.name.includes(keyword) ||
          shop.tags.some((tag) => tag.includes(keyword))
      );
    }
    if (this.data.activeTab === 'nearby') {
      list = list.sort((a, b) => a.waitTime - b.waitTime);
    } else {
      list = list.sort((a, b) => b.rating - a.rating);
    }

    this.filteredShops = list;
    if (resetPage) {
      this.setData(
        {
          page: 1,
          shopList: [],
          noMore: false
        },
        () => this.loadShopList()
      );
    } else {
      this.setData(
        {
          noMore: false
        },
        () => this.loadShopList()
      );
    }
  },

  // 加载商家列表
  loadShopList() {
    if (this.data.loading || this.data.noMore) return;
    this.setData({ loading: true });

    setTimeout(() => {
      const start = (this.data.page - 1) * PAGE_SIZE;
      const nextList = this.filteredShops.slice(start, start + PAGE_SIZE);
      const merged =
        this.data.page === 1
          ? nextList
          : [...this.data.shopList, ...nextList];
      const noMore = start + nextList.length >= this.filteredShops.length;

      this.setData({
        loading: false,
        noMore,
        shopList: this.decorateFavoriteState(merged)
      });
    }, 180);
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
    const shop = shopCatalog.find((item) => item.id === shopId);
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
