import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  async rewrites() {
    return [
      {
        source: '/api/v1/:path*',
        destination: 'https://ni1184ys72309.vicp.fun/api/v1/:path*',
      },
    ];
  },
};

export default nextConfig;
