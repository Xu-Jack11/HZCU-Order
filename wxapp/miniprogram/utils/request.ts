const BASE_URL = 'http://localhost:8080/api/v1';
const DEFAULT_TIMEOUT = 10000;

interface RequestOptions extends WechatMiniprogram.RequestOption {
  useMock?: boolean;
}

export const request = <T = any>(options: RequestOptions): Promise<T> => {
  return new Promise((resolve, reject) => {
    const token = wx.getStorageSync('token');

    wx.request({
      ...options,
      url: options.url.startsWith('http') ? options.url : `${BASE_URL}${options.url}`,
      timeout: options.timeout || DEFAULT_TIMEOUT,
      header: {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        ...options.header
      },
      success: (res) => {
        if (res.statusCode >= 200 && res.statusCode < 300) {
          resolve(res.data as T);
        } else if (res.statusCode === 401) {
          wx.removeStorageSync('token');
          reject({ statusCode: 401, message: 'Unauthorized' });
        } else {
          reject(res.data || res);
        }
      },
      fail: (err) => reject(err)
    });
  });
};
