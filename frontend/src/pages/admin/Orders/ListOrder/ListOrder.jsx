
import React, { useContext, useEffect, useState } from 'react'
import { StoreContext } from '../../../../context/StoreContext'
import "./ListOrder.css"
import { toast, ToastContainer } from 'react-toastify'
import { images } from '../../../../assets/images'
import NotFound from "../../../../components/NotFound/NotFound"
import axios from 'axios'
import { exportToPDF } from '../../../../services/Export'
import Popup from 'reactjs-popup'
import moment from 'moment'
import OrderDetail from '../../../../components/OrderDetail/OrderDetail'
import ReactPaginate from 'react-paginate'
import { useLocation } from 'react-router-dom'

const ListOrder = () => {

    const [adminOrders, setAdminOrders] = useState([])
    const { profileInfo } = useContext(StoreContext);
    const [updatingOrderId, setUpdatingOrderId] = useState(null);
    const [pageCount, setPageCount] = useState('1')
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const currentPage = parseInt(queryParams.get('page')) || 1;
    const BASE_URL = "http://localhost:8080"

    const [orderSearch, setOrderSearch] = useState({
        search: "",
        year_month: "",
        day: "",
        status: "pending"
    })

    const [type_date_search, setType_date_search] = useState('month')

    const token = localStorage.getItem('token');

    useEffect(() => {
        const fetchAllOrder = async () => {
            try {
                if (token) {
                    const res = await axios.get(`${BASE_URL}/admin/orders?page=${currentPage}`, {
                        headers: {
                            Authorization: `Bearer ${token}`
                        }
                    })
                    if (res.data.statusCode === 200) {
                        setAdminOrders(res.data.dataList);
                        setPageCount(prev => Math.ceil(res.data.amountAllData / 10))
                    } else {
                        throw new Error(res.data.message)
                    }
                }

            } catch (e) {
                console.log(e.message);
            }
        }
        fetchAllOrder()
    }, []);

    const updateOrderStatus = async (orderId, newStatus) => {
        // Cập nhật ngay trạng thái mới trên UI
        const previousOrders = [...adminOrders];

        try {
            const params = new URLSearchParams();
            params.append('status', newStatus);

            const res = await axios.put(
                `http://localhost:8080/admin/orders/${orderId}`,
                params,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                }
            );

            if (res.status !== 200 || res.data.statusCode !== 200) {
                throw new Error('Failed to update order status');
            } else {
                setAdminOrders((orders) =>
                    orders.map((order) =>
                        order.id === orderId ? { ...order, status: newStatus } : order
                    )
                );
            }

            toast.success('Updated order status successfully');
            setUpdatingOrderId(null)
        } catch (error) {
            console.error("Error updating order status:", error);
            toast.error('Error updating order status');

            // Hoàn nguyên lại trạng thái cũ khi có lỗi
            setAdminOrders(previousOrders);
        }
    }

    const VNDONG = (number) => {
        return number.toLocaleString('it-IT', { style: 'currency', currency: 'VND' });
    };

    const handleSubmitSearchOrder = async (e) => {
        e.preventDefault();


        const searching = async () => {
            try {
                const res = await axios.post(`${BASE_URL}/admin/orders/search`, orderSearch, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                })
                if (res.data.statusCode === 200) {
                    setAdminOrders(res.data.dataList)
                    setPageCount(null)
                    toast.success(res.data.message)
                } else {
                    throw new Error(res.data.message)
                }
            } catch (e) {
                toast.error(e.message)
            }
        }
        searching()

    }

    return (
        <div>
            <div className="order add">
                <div className='order_search_container'>
                    <h3>Tìm kiếm đơn hàng</h3>
                    <div className='checkbox_container'>
                        <label htmlFor="month_type">Theo tháng</label>
                        <input id='month_type' value="month" onChange={(e) => {
                            setType_date_search(prev => e.target.value)
                            setOrderSearch(prev => ({ ...prev, day: "" }))
                        }} type="radio" name='type_date_search' />

                        <label htmlFor="date_type">Theo ngày</label>
                        <input id='date_type' value="date" onChange={(e) => {
                            setType_date_search(prev => e.target.value)
                            setOrderSearch(prev => ({ ...prev, year_month: "" }))
                        }} type="radio" name='type_date_search' />
                    </div>
                    <form className='order_filter_form'>
                        <input onChange={(e) => setOrderSearch(prev => ({ ...prev, search: e.target.value }))} type="text" placeholder='Tìm kiếm mã đơn hàng' />
                        {
                            type_date_search === "date" ?
                                <input type="date" name='date' onChange={(e) => setOrderSearch(prev => ({ ...prev, day: e.target.value }))} />
                                :
                                <input type="month" name='year_month' onChange={(e) => setOrderSearch(prev => ({ ...prev, year_month: e.target.value }))} />
                        }
                        <select
                            onChange={(e) => setOrderSearch(prev => ({ ...prev, status: e.target.value }))}
                            name="status"
                            id="status"
                        >
                            <option value="paid">-- Chọn trạng thái đơn hàng</option>
                            <option value="paid">Đã thanh toán</option>
                            <option value="processed">Đã xử lý</option>
                            <option value="pending">Đang xử lý</option>
                            <option value="delivered">Đã giao</option>
                            <option value="canceled">Đã hủy</option>
                        </select>
                        <button onClick={handleSubmitSearchOrder} type='submit'>Tìm kiếm</button>
                    </form>
                </div>

                <h3>Các đơn đặt hàng</h3>
                {
                    adminOrders?.length > 0
                        ?
                        <div className="order-list">
                            {
                                adminOrders?.map((order) => (
                                    <div className="order-item" key={order.id}>
                                        <img src={images.parcel_icon} alt="parcel_icon" />
                                        <p className=''>Mã đơn hàng: {order.id}</p>
                                        <p className="order-item-tech">
                                            {
                                                order?.details?.map((item, index) => {
                                                    if (index === order.details.length - 1) {
                                                        return item?.product?.name + " x " + item?.quantity;
                                                    }
                                                    return item?.product?.name + " x " + item?.quantity + ", ";
                                                })
                                            }
                                        </p>
                                        <div className="">
                                            <p className="order-item-tech">Email: {order.email}</p>
                                            <p className="order-item-tech">Địa chỉ: {order.shippingAddress}</p>
                                            <p className="order-item-tech">SĐT: {order.phone}</p>
                                        </div>
                                        <p>Vật phẩm: {order.details.length}</p>
                                        <p>Ngày: {moment(order.orderDate).format('DD-MM-YYYY HH:mm:ss')}</p>
                                        <p>{VNDONG(order.totalAmount)}</p>
                                        <p>Phương thức thanh toán: {order.paymentMethod}</p>
                                        <select
                                            onChange={(e) => {
                                                setUpdatingOrderId(order.id); // Đặt ID của đơn hàng đang được cập nhật
                                                updateOrderStatus(order.id, e.target.value);
                                            }}
                                            value={order.status}
                                            disabled={updatingOrderId === order.id} // Vô hiệu hóa trong khi cập nhật
                                        >
                                            <option value="paid">Đã thanh toán</option>
                                            <option value="processed">Đã xử lý</option>
                                            <option value="pending">Đang xử lý</option>
                                            <option value="delivered">Đã giao</option>
                                            <option value="canceled">Đã hủy</option>
                                        </select>
                                        <button onClick={() => exportToPDF([order])}>Xuất hóa đơn PDF</button>
                                        <Popup
                                            modal
                                            nested
                                            trigger={
                                                <button>Chi tiết</button>
                                            }
                                        >
                                            {
                                                close => <OrderDetail close={close} order={order} />
                                            }
                                        </Popup>
                                    </div>
                                ))
                            }
                        </div>
                        : <NotFound />
                }
            </div>
            {
                pageCount !== null &&
                <ReactPaginate
                    className='pagination_order'
                    breakLabel="..."
                    nextLabel=">>"
                    onPageChange={(e) => window.location.href = `/admin/orders?page=${e.selected + 1}`}
                    pageRangeDisplayed={5}
                    pageCount={pageCount}
                    previousLabel="<<"
                    renderOnZeroPageCount={null}
                    forcePage={currentPage - 1}
                />
            }
            <ToastContainer hideProgressBar draggable stacked position='top-center' autoClose={1000} />
        </div>
    );
}

export default ListOrder;