// 默认指向本地启动的 Spring Boot 后端，发布时可替换为线上域名
const BASE_URL = 'http://localhost:8080/api/v1';
const DEFAULT_TIMEOUT = 8000;

interface RequestOptions extends WechatMiniprogram.RequestOption {
  useMock?: boolean;
}

export const request = <T = any>(options: RequestOptions): Promise<T> => {
  return new Promise((resolve, reject) => {
    wx.request({
      ...options,
      url: `${BASE_URL}${options.url}`,
      timeout: options.timeout || DEFAULT_TIMEOUT,
      header: {
        'Content-Type': 'application/json',
        'Cache-Control': 'no-cache',
        Pragma: 'no-cache',
        Authorization: wx.getStorageSync('token') ? `Bearer ${wx.getStorageSync('token')}` : '',
        ...options.header
      },
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data as T);
        } else {
          reject(res);
        }
      },
      fail: (err) => reject(err)
    });
  });
};
