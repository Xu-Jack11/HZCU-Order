'use client';

import Link from 'next/link';
import { usePathname, useRouter } from 'next/navigation';
import { LayoutDashboard, UtensilsCrossed, BarChart3, LogOut } from 'lucide-react';
import styles from './Sidebar.module.css';

const menuItems = [
    { name: '工作台', href: '/dashboard', icon: LayoutDashboard },
    { name: '菜品管理', href: '/dashboard/menu', icon: UtensilsCrossed },
    { name: '数据统计', href: '/dashboard/stats', icon: BarChart3 },
];

export default function Sidebar() {
    const pathname = usePathname();
    const router = useRouter();

    const handleLogout = () => {
        // In a real app, you would clear auth tokens here
        // For demo, just navigate to login
        router.push('/login');
    };

    return (
        <aside className={styles.sidebar}>
            <div className={styles.header}>
                HZCU Merchant
            </div>
            <nav className={styles.nav}>
                {menuItems.map((item) => {
                    const Icon = item.icon;
                    const isActive = item.href === '/dashboard'
                        ? pathname === '/dashboard'
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

