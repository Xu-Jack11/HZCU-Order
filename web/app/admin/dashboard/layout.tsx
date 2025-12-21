'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import AdminSidebar from '@/components/AdminSidebar';
import styles from './layout.module.css';

export default function AdminDashboardLayout({
    children,
}: {
    children: React.ReactNode;
}) {
    const router = useRouter();
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [adminName, setAdminName] = useState('System Admin');
    const [userInitial, setUserInitial] = useState('A');

    useEffect(() => {
        // Check if user is logged in as admin
        const token = localStorage.getItem('token');
        const role = localStorage.getItem('role');
        const userStr = localStorage.getItem('user');
        
        if (!token || role !== 'ROLE_ADMIN') {
            // No token or not admin, redirect to login
            router.push('/login');
            return;
        }

        setIsAuthenticated(true);
        
        if (userStr) {
            try {
                const user = JSON.parse(userStr);
                if (user.username) {
                    setAdminName(user.username);
                    setUserInitial(user.username.charAt(0).toUpperCase());
                }
            } catch (e) {
                console.error('Failed to parse user info', e);
            }
        }
    }, [router]);

    // Don't render until authentication check is complete
    if (!isAuthenticated) {
        return (
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
                <span>加载中...</span>
            </div>
        );
    }

    return (
        <div className={styles.container}>
            <AdminSidebar />
            <main className={styles.main}>
                <header className={styles.header}>
                    <h1 className={styles.title}>平台管理中心</h1>
                    <div className={styles.userProfile}>
                        <span>{adminName}</span>
                        <div className={styles.avatar}>{userInitial}</div>
                    </div>
                </header>
                <div className={styles.content}>
                    {children}
                </div>
            </main>
        </div>
    );
}
