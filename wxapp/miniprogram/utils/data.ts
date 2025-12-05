export interface ShopItem {
  id: number;
  name: string;
  logo: string;
  rating: number;
  monthlySales: number;
  waitTime: number;
  distance: string;
  minPrice: number;
  tags: string[];
  categoryIds: number[];
  notice?: string;
  address?: string;
  businessHours?: string;
  phone?: string;
}

export const shopCatalog: ShopItem[] = [
  {
    id: 1,
    name: '肯德基（城院店）',
    logo: '/images/shops/kfc.png',
    rating: 4.8,
    monthlySales: 719,
    waitTime: 15,
    distance: '1.8km',
    minPrice: 0,
    tags: ['20减8', '30减12'],
    categoryIds: [1, 2, 4],
    notice: '共赏元月一轮，喜迎中秋良宵。',
    address: '浙江省杭州市拱墅区湖州街51号',
    businessHours: '09:00-22:00',
    phone: '0571-88888888'
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
    tags: ['45减30', '75减45'],
    categoryIds: [5, 6],
    notice: '今日牛肉分量加倍，欢迎堂食。',
    address: '浙大城院南校区学而路15号',
    businessHours: '10:30-21:30',
    phone: '0571-88998899'
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
    tags: ['20减12', '35减19'],
    categoryIds: [3, 4],
    notice: '新品豆子上架，下午场限时买一赠一。',
    address: '浙江省杭州市湖州街51号-1',
    businessHours: '08:00-22:00',
    phone: '0571-88776655'
  }
];

export const getShopById = (shopId: number) => {
  return shopCatalog.find((shop) => shop.id === shopId) || null;
};

export type CouponScope = 'all' | 'shop';

export interface CouponItem {
  id: number;
  title: string;
  desc: string;
  discount: number;
  threshold: number;
  scope: CouponScope;
  shopId?: number;
  validDate: string;
}

export const defaultCoupons: CouponItem[] = [
  {
    id: 1,
    title: '校园午餐券',
    desc: '全平台满30减8',
    discount: 8,
    threshold: 30,
    scope: 'all',
    validDate: '有效期至 2024-12-31'
  },
  {
    id: 2,
    title: 'KFC 学生专享',
    desc: '指定店满45减15',
    discount: 15,
    threshold: 45,
    scope: 'shop',
    shopId: 1,
    validDate: '有效期至 2024-12-15'
  },
  {
    id: 3,
    title: '咖啡加油券',
    desc: '咖啡饮品满20减5',
    discount: 5,
    threshold: 20,
    scope: 'shop',
    shopId: 3,
    validDate: '有效期至 2025-01-10'
  }
];
