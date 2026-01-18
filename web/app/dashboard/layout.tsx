'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Sidebar from '@/components/Sidebar';
import styles from './layout.module.css';

export default function DashboardLayout({
    children,
}: {
    children: React.ReactNode;
}) {
    const router = useRouter();
    const [canteenName, setCanteenName] = useState('加载中...');
    const [userInitial, setUserInitial] = useState('?');
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    useEffect(() => {
        // Check if user is logged in
        const token = localStorage.getItem('token');
        const userStr = localStorage.getItem('user');
        
        if (!token || !userStr) {
            // No token or user info, redirect to login
            router.push('/login');
            return;
        }

        setIsAuthenticated(true);
        
        try {
            const user = JSON.parse(userStr);
            setCanteenName(user.canteenName || '系统管理');
            if (user.canteenName) {
                setUserInitial(user.canteenName.charAt(0));
            } else if (user.username) {
                setUserInitial(user.username.charAt(0).toUpperCase());
            }
        } catch (e) {
            console.error('Failed to parse user info', e);
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
            <Sidebar />
            <main className={styles.main}>
                <header className={styles.header}>
                    <h1 className={styles.title}>商家管理后台</h1>
                    <div className={styles.userProfile}>
                        <span>{canteenName}</span>
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
