'use client';

import { useState, useRef } from 'react';
import { Plus, Edit2, Trash2, X, Search, Upload, ImageIcon } from 'lucide-react';
import styles from './page.module.css';

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

const initialDishes: Dish[] = [
    { id: '1', name: '红烧肉套餐', price: 25.0, category: '套餐', isAvailable: true, sales: 120, description: '精选五花肉，配米饭和时蔬', image: 'https://images.unsplash.com/photo-1623689046286-adce87bfd64c?w=200&h=200&fit=crop' },
    { id: '2', name: '番茄炒蛋', price: 12.0, category: '热菜', isAvailable: true, sales: 85, description: '新鲜番茄搭配农家土鸡蛋', image: 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=200&h=200&fit=crop' },
    { id: '3', name: '牛肉面', price: 18.0, category: '面食', isAvailable: false, sales: 200, description: '手工拉面配卤牛肉', image: 'https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=200&h=200&fit=crop' },
    { id: '4', name: '可乐', price: 3.0, category: '饮料', isAvailable: true, sales: 300, description: '冰镇可口可乐 330ml', image: 'https://images.unsplash.com/photo-1629203851122-3726ecdf080e?w=200&h=200&fit=crop' },
    { id: '5', name: '宫保鸡丁', price: 16.0, category: '热菜', isAvailable: true, sales: 95, description: '川味经典，微辣口感', image: 'https://images.unsplash.com/photo-1525755662778-989d0524087e?w=200&h=200&fit=crop' },
    { id: '6', name: '蛋炒饭', price: 10.0, category: '主食', isAvailable: true, sales: 180, description: '粒粒分明，蛋香浓郁', image: 'https://images.unsplash.com/photo-1603133872878-684f208fb84b?w=200&h=200&fit=crop' },
];

const categories = ['全部', '套餐', '热菜', '面食', '主食', '饮料'];

export default function MenuPage() {
    const [dishes, setDishes] = useState<Dish[]>(initialDishes);
    const [showModal, setShowModal] = useState(false);
    const [editingDish, setEditingDish] = useState<Dish | null>(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedCategory, setSelectedCategory] = useState('全部');
    const fileInputRef = useRef<HTMLInputElement>(null);

    // Form state
    const [formData, setFormData] = useState({
        name: '',
        price: '',
        category: '热菜',
        description: '',
        image: ''
    });

    const toggleStatus = (id: string) => {
        setDishes(prev => prev.map(d =>
            d.id === id ? { ...d, isAvailable: !d.isAvailable } : d
        ));
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

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (editingDish) {
            // Edit existing dish
            setDishes(prev => prev.map(d =>
                d.id === editingDish.id
                    ? { ...d, name: formData.name, price: parseFloat(formData.price), category: formData.category, description: formData.description, image: formData.image }
                    : d
            ));
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

    const filteredDishes = dishes.filter(dish => {
        const matchesSearch = dish.name.toLowerCase().includes(searchTerm.toLowerCase());
        const matchesCategory = selectedCategory === '全部' || dish.category === selectedCategory;
        return matchesSearch && matchesCategory;
    });

    return (
        <div>
            <div className={styles.header}>
                <h2 className={styles.title}>菜品管理</h2>
                <button className={styles.addButton} onClick={openAddModal}>
                    <Plus size={16} /> 新增菜品
                </button>
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
                                        <div className={styles.dishName}>{dish.name}</div>
                                        {dish.description && <div className={styles.dishDesc}>{dish.description}</div>}
                                    </td>
                                    <td><span className={styles.categoryBadge}>{dish.category}</span></td>
                                    <td className={styles.price}>¥{dish.price.toFixed(2)}</td>
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
