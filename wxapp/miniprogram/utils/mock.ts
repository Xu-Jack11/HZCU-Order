import { ShopItem, shopCatalog } from './data';

export interface GoodsItem {
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

export interface Category {
  id: number;
  name: string;
  goods: GoodsItem[];
}

export const mockCanteenList = (params: { keyword?: string; categoryId?: number; page: number; pageSize: number }) => {
  const { keyword, categoryId, page, pageSize } = params;
  let list = shopCatalog.slice();
  if (categoryId) {
    list = list.filter((shop) => shop.categoryIds.includes(categoryId));
  }
  if (keyword) {
    list = list.filter((shop) => shop.name.includes(keyword) || shop.tags.some((tag) => tag.includes(keyword)));
  }
  const start = (page - 1) * pageSize;
  const data = list.slice(start, start + pageSize);
  return {
    list: data,
    total: list.length
  };
};

export const mockCanteenDetail = (shopId: number) => {
  const shop = shopCatalog.find((item) => item.id === shopId) || shopCatalog[0];
  const categories: Category[] = [
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
  ];

  const comments = [
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
  ];

  return {
    shopInfo: shop,
    categories,
    comments
  };
};

export const mockOrders = (params: { status: string; page: number; pageSize: number }) => {
  const all = [
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
  ];

  const filtered = params.status === 'all' ? all : all.filter((item) => item.status === params.status);
  const start = (params.page - 1) * params.pageSize;
  const data = filtered.slice(start, start + params.pageSize);
  return {
    list: data,
    total: filtered.length
  };
};

export const mockCreateOrder = () => {
  return {
    orderId: Date.now(),
    status: 'pending'
  };
};
