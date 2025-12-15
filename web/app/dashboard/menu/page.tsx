'use client';

import { useEffect, useMemo, useRef, useState } from 'react';
import { Plus, Edit2, Trash2, X, Search, Upload, ImageIcon } from 'lucide-react';
import styles from './page.module.css';
import { api } from '@/lib/api';

interface Dish {
    id: string;
    name: string;
    price: number;
    category: string;
    isAvailable: boolean;
    sales: number;
    description?: string;
    image?: string;
}

// 记录未保存的改动
const categoriesDefault = ['全部'];

export default function MenuPage() {
    const [dishes, setDishes] = useState<Dish[]>([]);
    const [showModal, setShowModal] = useState(false);
    const [editingDish, setEditingDish] = useState<Dish | null>(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedCategory, setSelectedCategory] = useState('全部');
    const [categories, setCategories] = useState<string[]>(categoriesDefault);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    const [shopId, setShopId] = useState<string>('');
    const [pendingEdits, setPendingEdits] = useState<Record<string, { name?: string; price?: number; category?: string }>>({});
    const fileInputRef = useRef<HTMLInputElement>(null);

    // Form state
    const [formData, setFormData] = useState({
        name: '',
        price: '',
        category: '热菜',
        description: '',
        image: ''
    });

    const toggleStatus = async (id: string) => {
        const target = dishes.find(d => d.id === id);
        if (!target) return;
        try {
            await api.updateDishAvailability(id, !target.isAvailable);
            setDishes(prev => prev.map(d =>
                d.id === id ? { ...d, isAvailable: !d.isAvailable } : d
            ));
        } catch (e) {
            alert('更新上架状态失败');
        }
    };

    const handleDelete = (id: string) => {
        if (confirm('确定删除该菜品吗？')) {
            setDishes(prev => prev.filter(d => d.id !== id));
        }
    };

    const openAddModal = () => {
        setEditingDish(null);
        setFormData({ name: '', price: '', category: '热菜', description: '', image: '' });
        setShowModal(true);
    };

    const openEditModal = (dish: Dish) => {
        setEditingDish(dish);
        setFormData({
            name: dish.name,
            price: dish.price.toString(),
            category: dish.category,
            description: dish.description || '',
            image: dish.image || ''
        });
        setShowModal(true);
    };

    // 行内编辑：记录改动并同步到列表展示
    const setEditField = (dishId: string, field: 'name' | 'price' | 'category', value: any) => {
        setPendingEdits(prev => ({
            ...prev,
            [dishId]: { ...(prev[dishId] || {}), [field]: value }
        }));
        setDishes(prev => prev.map(d => d.id === dishId ? { ...d, [field]: value } : d));
    };

    const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (file) {
            // In a real app, you would upload to a server here
            // For demo, we'll use a local URL
            const reader = new FileReader();
            reader.onloadend = () => {
                setFormData({ ...formData, image: reader.result as string });
            };
            reader.readAsDataURL(file);
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const payload = {
            name: formData.name,
            price: parseFloat(formData.price),
            description: formData.description,
            image: formData.image,
            // categoryId 需要从后端分类映射，这里暂留
        } as any;
        if (editingDish) {
            try {
                await api.updateDish(editingDish.id, payload);
                setDishes(prev => prev.map(d =>
                    d.id === editingDish.id
                        ? { ...d, name: formData.name, price: parseFloat(formData.price), category: formData.category, description: formData.description, image: formData.image }
                        : d
                ));
            } catch (e) {
                alert('保存菜品失败');
                return;
            }
        } else {
            // Add new dish
            const newDish: Dish = {
                id: Date.now().toString(),
                name: formData.name,
                price: parseFloat(formData.price),
                category: formData.category,
                description: formData.description,
                image: formData.image,
                isAvailable: true,
                sales: 0
            };
            setDishes(prev => [...prev, newDish]);
        }
        setShowModal(false);
    };

    // 保存单个菜的改动
    const onSaveDish = async (dishId: string) => {
        const edits = pendingEdits[dishId];
        if (!edits || Object.keys(edits).length === 0) {
            alert('没有改动需要保存');
            return;
        }
        try {
            await api.updateDish(dishId, edits);
            setPendingEdits(prev => {
                const next = { ...prev };
                delete next[dishId];
                return next;
            });
            alert('已保存');
        } catch (e: any) {
            alert(e.message || '保存失败');
        }
    };

    // 保存全部改动
    const onSaveAll = async () => {
        const ids = Object.keys(pendingEdits);
        if (ids.length === 0) {
            alert('没有改动需要保存');
            return;
        }
        try {
            for (const id of ids) {
                await api.updateDish(id, pendingEdits[id]!);
            }
            setPendingEdits({});
            alert('全部改动已保存');
        } catch (e: any) {
            alert(e.message || '部分保存失败，请重试');
        }
    };

    // 初始化：选择一个商家并加载其菜品
    useEffect(() => {
        const init = async () => {
            setLoading(true);
            setError(null);
            try {
                const page = await api.listCanteensPaged(1, 1);
                const first = page.list?.[0];
                const sid = String(first?.id || '');
                setShopId(sid);
                if (sid) {
                    const cats = await api.getShopDishes(sid, 1, 10000);
                    const catNames = ['全部', ...cats.map(c => String(c.name))];
                    setCategories(catNames);
                    const items: Dish[] = cats.flatMap(c => (c.goods || []).map(g => ({
                        id: String(g.id),
                        name: g.name,
                        price: g.price,
                        category: String(c.name),
                        isAvailable: true,
                        sales: (g.monthlySales as any) || 0,
                        description: g.description,
                        image: g.image,
                    })));
                    setDishes(items);
                }
            } catch (e: any) {
                setError(e?.message || '加载菜品失败');
            } finally {
                setLoading(false);
            }
        };
        init();
    }, []);

    const filteredDishes = dishes.filter(dish => {
        const matchesSearch = dish.name.toLowerCase().includes(searchTerm.toLowerCase());
        const matchesCategory = selectedCategory === '全部' || dish.category === selectedCategory;
        return matchesSearch && matchesCategory;
    });

    return (
        <div>
            <div className={styles.header}>
                <h2 className={styles.title}>菜品管理</h2>
                <div style={{ display: 'flex', gap: 8 }}>
                    <button className={styles.addButton} onClick={openAddModal}>
                        <Plus size={16} /> 新增菜品
                    </button>
                    <button className={styles.addButton} onClick={onSaveAll}>
                        保存全部改动
                    </button>
                </div>
            </div>

            {/* Filters */}
            <div className={styles.filters}>
                <div className={styles.searchBox}>
                    <Search size={18} className={styles.searchIcon} />
                    <input
                        type="text"
                        placeholder="搜索菜品..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className={styles.searchInput}
                    />
                </div>
                <div className={styles.categoryTabs}>
                    {categories.map(cat => (
                        <button
                            key={cat}
                            className={`${styles.categoryTab} ${selectedCategory === cat ? styles.categoryTabActive : ''}`}
                            onClick={() => setSelectedCategory(cat)}
                        >
                            {cat}
                        </button>
                    ))}
                </div>
            </div>

            <div className={styles.tableWrapper}>
                {error && (
                    <div style={{ color: '#c00', padding: '0.5rem 1rem' }}>{error}</div>
                )}
                {loading && (
                    <div style={{ color: '#666', padding: '0.5rem 1rem' }}>加载中...</div>
                )}
                <table className={styles.table}>
                    <thead>
                        <tr>
                            <th style={{ width: '80px' }}>图片</th>
                            <th>菜品名称</th>
                            <th>分类</th>
                            <th>价格</th>
                            <th>销量</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filteredDishes.length === 0 ? (
                            <tr>
                                <td colSpan={7} style={{ textAlign: 'center', padding: '3rem', color: '#94a3b8' }}>
                                    暂无符合条件的菜品
                                </td>
                            </tr>
                        ) : (
                            filteredDishes.map(dish => (
                                <tr key={dish.id}>
                                    <td>
                                        <div className={styles.dishImage}>
                                            {dish.image ? (
                                                <img src={dish.image} alt={dish.name} />
                                            ) : (
                                                <div className={styles.noImage}>
                                                    <ImageIcon size={20} />
                                                </div>
                                            )}
                                        </div>
                                    </td>
                                     <td>
                                         <input
                                             className={styles.inlineInput}
                                             value={dish.name}
                                             onChange={(e) => setEditField(dish.id, 'name', e.target.value)}
                                         />
                                         {dish.description && <div className={styles.dishDesc}>{dish.description}</div>}
                                     </td>
                                    <td><span className={styles.categoryBadge}>{dish.category}</span></td>
                                     <td className={styles.price}>
                                         ¥
                                         <input
                                             className={styles.inlineInput}
                                             type="number"
                                             step="0.01"
                                             value={dish.price}
                                             onChange={(e) => setEditField(dish.id, 'price', parseFloat(e.target.value || '0'))}
                                         />
                                     </td>
                                    <td>{dish.sales}</td>
                                    <td>
                                        <button
                                            className={`${styles.statusToggle} ${dish.isAvailable ? styles.statusOn : styles.statusOff}`}
                                            onClick={() => toggleStatus(dish.id)}
                                        >
                                            {dish.isAvailable ? '上架中' : '已下架'}
                                        </button>
                                    </td>
                                    <td>
                                         <button className={styles.actionBtn} onClick={() => onSaveDish(dish.id)}>
                                             保存
                                         </button>
                                         <button className={styles.actionBtn} onClick={() => openEditModal(dish)}>
                                            <Edit2 size={14} /> 编辑
                                        </button>
                                        <button className={`${styles.actionBtn} ${styles.deleteBtn}`} onClick={() => handleDelete(dish.id)}>
                                            <Trash2 size={14} /> 删除
                                        </button>
                                    </td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            </div>

            {/* Modal */}
            {showModal && (
                <div className={styles.modalOverlay} onClick={() => setShowModal(false)}>
                    <div className={styles.modal} onClick={e => e.stopPropagation()}>
                        <div className={styles.modalHeader}>
                            <h3>{editingDish ? '编辑菜品' : '新增菜品'}</h3>
                            <button className={styles.closeBtn} onClick={() => setShowModal(false)}>
                                <X size={20} />
                            </button>
                        </div>
                        <form onSubmit={handleSubmit} className={styles.form}>
                            {/* Image Upload */}
                            <div className={styles.formGroup}>
                                <label>菜品图片</label>
                                <div className={styles.imageUploadArea}>
                                    {formData.image ? (
                                        <div className={styles.imagePreview}>
                                            <img src={formData.image} alt="预览" />
                                            <button
                                                type="button"
                                                className={styles.removeImage}
                                                onClick={() => setFormData({ ...formData, image: '' })}
                                            >
                                                <X size={16} />
                                            </button>
                                        </div>
                                    ) : (
                                        <div
                                            className={styles.uploadPlaceholder}
                                            onClick={() => fileInputRef.current?.click()}
                                        >
                                            <Upload size={32} />
                                            <span>点击上传图片</span>
                                            <span className={styles.uploadHint}>支持 JPG、PNG 格式</span>
                                        </div>
                                    )}
                                    <input
                                        ref={fileInputRef}
                                        type="file"
                                        accept="image/*"
                                        onChange={handleImageUpload}
                                        style={{ display: 'none' }}
                                    />
                                </div>
                            </div>

                            <div className={styles.formGroup}>
                                <label>菜品名称</label>
                                <input
                                    type="text"
                                    value={formData.name}
                                    onChange={e => setFormData({ ...formData, name: e.target.value })}
                                    required
                                    placeholder="请输入菜品名称"
                                />
                            </div>
                            <div className={styles.formRow}>
                                <div className={styles.formGroup}>
                                    <label>价格 (元)</label>
                                    <input
                                        type="number"
                                        step="0.01"
                                        value={formData.price}
                                        onChange={e => setFormData({ ...formData, price: e.target.value })}
                                        required
                                        placeholder="0.00"
                                    />
                                </div>
                                <div className={styles.formGroup}>
                                    <label>分类</label>
                                    <select
                                        value={formData.category}
                                        onChange={e => setFormData({ ...formData, category: e.target.value })}
                                    >
                                        {categories.filter(c => c !== '全部').map(cat => (
                                            <option key={cat} value={cat}>{cat}</option>
                                        ))}
                                    </select>
                                </div>
                            </div>
                            <div className={styles.formGroup}>
                                <label>描述</label>
                                <textarea
                                    value={formData.description}
                                    onChange={e => setFormData({ ...formData, description: e.target.value })}
                                    placeholder="请输入菜品描述（可选）"
                                    rows={3}
                                />
                            </div>
                            <div className={styles.formActions}>
                                <button type="button" className={styles.cancelBtn} onClick={() => setShowModal(false)}>
                                    取消
                                </button>
                                <button type="submit" className={styles.submitBtn}>
                                    {editingDish ? '保存修改' : '添加菜品'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}
