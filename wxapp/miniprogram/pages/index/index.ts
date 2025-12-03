// index.ts
// 首页 - 商家列表

Page({
  data: {
    location: '浙大城市学院（南校区）',
    searchKeyword: '',
    activeTab: 'nearby',
    loading: false,
    noMore: false,
    page: 1,
    categories: [
      { id: 1, name: '汉堡披萨', icon: '/images/category/burger.png' },
      { id: 2, name: '炸鸡薯条', icon: '/images/category/chicken.png' },
      { id: 3, name: '水果', icon: '/images/category/fruit.png' },
      { id: 4, name: '甜品饮料', icon: '/images/category/drink.png' },
      { id: 5, name: '中餐', icon: '/images/category/chinese.png' },
      { id: 6, name: '面食', icon: '/images/category/noodle.png' },
    ],
    shopList: [
      {
        id: 1,
        name: '肯德基（城院店）',
        logo: '/images/shops/kfc.png',
        rating: 4.8,
        monthlySales: 719,
        waitTime: 15,
        distance: '1.8km',
        minPrice: 0,
        tags: ['20减8', '30减12']
      },
      {
        id: 2,
        name: '兰州拉面',
        logo: '/images/shops/lamian.png',
        rating: 4.7,
        monthlySales: 1750,
        waitTime: 10,
        distance: '2.4km',
        minPrice: 0,
        tags: ['45减30', '75减45']
      },
      {
        id: 3,
        name: '库迪咖啡（城院南校区店）',
        logo: '/images/shops/cotti.png',
        rating: 4.8,
        monthlySales: 1613,
        waitTime: 12,
        distance: '1.7km',
        minPrice: 0,
        tags: ['20减12', '35减19']
      },
    ]
  },

  onLoad() {
    this.loadShopList();
  },

  onShow() {
    // 页面显示时刷新
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
    const keyword = this.data.searchKeyword;
    if (keyword) {
      // 执行搜索逻辑
      this.loadShopList(keyword);
    }
  },

  // 分类点击
  onCategoryTap(e: any) {
    const categoryId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/category/category?id=${categoryId}`
    });
  },

  // 筛选标签切换
  onTabChange(e: any) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({
      activeTab: tab,
      page: 1,
      shopList: [],
      noMore: false
    });
    this.loadShopList();
  },

  // 加载商家列表
  loadShopList(keyword?: string) {
    if (this.data.loading || this.data.noMore) return;
    
    this.setData({ loading: true });
    
    // 模拟API请求
    setTimeout(() => {
      // 这里应该调用实际的API
      this.setData({
        loading: false,
        // 实际项目中这里会合并新数据
      });
    }, 500);
  },

  // 加载更多
  loadMore() {
    if (this.data.loading || this.data.noMore) return;
    this.setData({
      page: this.data.page + 1
    });
    this.loadShopList();
  },

  // 跳转到商家详情
  goToShop(e: any) {
    const shopId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/shop/shop?id=${shopId}`
    });
  }
});
