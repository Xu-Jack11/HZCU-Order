'use client';

import { useEffect, useState } from 'react';
import Sidebar from '@/components/Sidebar';
import styles from './layout.module.css';

export default function DashboardLayout({
    children,
}: {
    children: React.ReactNode;
}) {
    const [canteenName, setCanteenName] = useState('加载中...');
    const [userInitial, setUserInitial] = useState('?');

    useEffect(() => {
        const userStr = localStorage.getItem('user');
        if (userStr) {
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
        }
    }, []);

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
