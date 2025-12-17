import AdminSidebar from '@/components/AdminSidebar';
import styles from './layout.module.css';

export default function AdminDashboardLayout({
    children,
}: {
    children: React.ReactNode;
}) {
    return (
        <div className={styles.container}>
            <AdminSidebar />
            <main className={styles.main}>
                <header className={styles.header}>
                    <h1 className={styles.title}>平台管理中心</h1>
                    <div className={styles.userProfile}>
                        <span>System Admin</span>
                        <div className={styles.avatar}>A</div>
                    </div>
                </header>
                <div className={styles.content}>
                    {children}
                </div>
            </main>
        </div>
    );
}
