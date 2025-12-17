/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  // 开发阶段将 /api 代理到后端服务，避免 CORS
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: process.env.NEXT_PUBLIC_API_BASE
          ? `${process.env.NEXT_PUBLIC_API_BASE.replace(/\/$/, '')}/:path*`
          : 'http://localhost:8080/api/v1/:path*',
      },
    ];
  },
};

export default nextConfig;