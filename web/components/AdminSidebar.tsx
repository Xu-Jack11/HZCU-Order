'use client';

import Link from 'next/link';
import { usePathname, useRouter } from 'next/navigation';
import { LayoutDashboard, Store, Users, LogOut, BarChart3, Settings } from 'lucide-react';
import styles from './AdminSidebar.module.css';

const menuItems = [
    { name: '平台概览', href: '/admin/dashboard', icon: LayoutDashboard },
    { name: '商家管理', href: '/admin/dashboard/canteens', icon: Store },
    { name: '用户管理', href: '/admin/dashboard/users', icon: Users },
    { name: '财务报表', href: '/admin/dashboard/finance', icon: BarChart3 },
    { name: '系统设置', href: '/admin/dashboard/settings', icon: Settings },
];

export default function AdminSidebar() {
    const pathname = usePathname();
    const router = useRouter();

    const handleLogout = () => {
        // Implement logout logic here
        router.push('/admin/login');
    };

    return (
        <aside className={styles.sidebar}>
            <div className={styles.header}>
                HZCU Admin
            </div>
            <nav className={styles.nav}>
                {menuItems.map((item) => {
                    const Icon = item.icon;
                    // Exact match for dashboard, startsWith for others to handle sub-routes
                    const isActive = item.href === '/admin/dashboard'
                        ? pathname === '/admin/dashboard'
                        : pathname.startsWith(item.href);

                    return (
                        <Link
                            key={item.href}
                            href={item.href}
                            className={`${styles.link} ${isActive ? styles.active : ''}`}
                        >
                            <Icon size={20} />
                            <span>{item.name}</span>
                        </Link>
                    );
                })}
            </nav>
            <div className={styles.footer}>
                <button className={styles.logoutBtn} onClick={handleLogout}>
                    <LogOut size={20} />
                    <span>退出登录</span>
                </button>
            </div>
        </aside>
    );
}
