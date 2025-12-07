'use client';

import { useState } from 'react';
import { Plus, Trash, Eye, Settings as SettingsIcon } from 'lucide-react';
import styles from './page.module.css';

type Tab = 'BANNERS' | 'NOTICES' | 'COUPONS' | 'ROLES' | 'LOGS';

export default function SettingsPage() {
    const [activeTab, setActiveTab] = useState<Tab>('BANNERS');

    const renderBanners = () => (
        <div>
            <div className={styles.sectionHeader}>
                <h3 className={styles.sectionTitle}>首页轮播图管理</h3>
                <button className={styles.addButton}><Plus size={16} /> 新增轮播图</button>
            </div>
            <div className={styles.list}>
                <div className={styles.listItem}>
                    <div className={styles.itemInfo}>
                        <span className={styles.itemTitle}>秋季美食节活动</span>
                        <span className={styles.itemMeta}>跳转链接: /pages/activity/autumn • 排序: 1</span>
                    </div>
                    <div className={styles.itemActions}>
                        <button className={styles.actionBtn}>编辑</button>
                        <button className={styles.actionBtn}>下架</button>
                    </div>
                </div>
                <div className={styles.listItem}>
                    <div className={styles.itemInfo}>
                        <span className={styles.itemTitle}>新学期食堂指南</span>
                        <span className={styles.itemMeta}>跳转链接: /pages/guide/new-term • 排序: 2</span>
                    </div>
                    <div className={styles.itemActions}>
                        <button className={styles.actionBtn}>编辑</button>
                        <button className={styles.actionBtn}>下架</button>
                    </div>
                </div>
            </div>
        </div>
    );

    const renderNotices = () => (
        <div>
            <div className={styles.sectionHeader}>
                <h3 className={styles.sectionTitle}>系统公告发布</h3>
                <button className={styles.addButton}><Plus size={16} /> 发布新公告</button>
            </div>
            <div className={styles.list}>
                <div className={styles.listItem}>
                    <div className={styles.itemInfo}>
                        <span className={styles.itemTitle}>关于系统维护的通知</span>
                        <span className={styles.itemMeta}>发布时间: 2025-12-01 • 类型: 系统通知</span>
                    </div>
                    <div className={styles.itemActions}>
                        <button className={styles.actionBtn}>撤回</button>
                    </div>
                </div>
            </div>
        </div>
    );

    return (
        <div>
            <h2 style={{ fontSize: '1.5rem', fontWeight: 600, marginBottom: '1.5rem', color: 'var(--foreground)' }}>系统设置</h2>

            <div className={styles.container}>
                <div className={styles.tabs}>
                    <div
                        className={`${styles.tab} ${activeTab === 'BANNERS' ? styles.activeTab : ''}`}
                        onClick={() => setActiveTab('BANNERS')}
                    >
                        轮播图配置
                    </div>
                    <div
                        className={`${styles.tab} ${activeTab === 'NOTICES' ? styles.activeTab : ''}`}
                        onClick={() => setActiveTab('NOTICES')}
                    >
                        公告管理
                    </div>
                    <div
                        className={`${styles.tab} ${activeTab === 'COUPONS' ? styles.activeTab : ''}`}
                        onClick={() => setActiveTab('COUPONS')}
                    >
                        平台优惠券
                    </div>
                    <div
                        className={`${styles.tab} ${activeTab === 'ROLES' ? styles.activeTab : ''}`}
                        onClick={() => setActiveTab('ROLES')}
                    >
                        角色权限
                    </div>
                    <div
                        className={`${styles.tab} ${activeTab === 'LOGS' ? styles.activeTab : ''}`}
                        onClick={() => setActiveTab('LOGS')}
                    >
                        操作日志
                    </div>
                </div>

                <div className={styles.content}>
                    {activeTab === 'BANNERS' && renderBanners()}
                    {activeTab === 'NOTICES' && renderNotices()}
                    {activeTab === 'COUPONS' && <div className="p-4 text-center text-gray-500">优惠券管理功能开发中...</div>}
                    {activeTab === 'ROLES' && <div className="p-4 text-center text-gray-500">角色权限管理功能开发中...</div>}
                    {activeTab === 'LOGS' && <div className="p-4 text-center text-gray-500">系统日志查看功能开发中...</div>}
                </div>
            </div>
        </div>
    );
}
