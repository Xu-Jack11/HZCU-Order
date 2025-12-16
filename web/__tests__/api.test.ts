import { api } from '../lib/api';

// 简单 mock request，确保 getShops 拼接的路径正确
// 由于项目中的 request 是通过 fetch 或自定义封装，这里用全局替身
(global as any).fetch = jest.fn().mockResolvedValue({
  ok: true,
  json: async () => ({ list: [], total: 0 }),
});

describe('api.getShops', () => {
  it('拼接分页参数到 /shops 路径', async () => {
    await api.getShops(2, 50);
    const calls = (global as any).fetch.mock.calls;
    const url = calls[0][0];
    expect(String(url)).toContain('/shops?page=2&pageSize=50');
  });
});
