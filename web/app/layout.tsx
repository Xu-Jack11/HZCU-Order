import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "商家管理系统 - HZCU Order",
  description: "杭州城市学院校园订餐系统商家端",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="zh-CN">
      <body>
        {children}
      </body>
    </html>
  );
}
