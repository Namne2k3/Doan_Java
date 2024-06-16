
import React, { useContext, useEffect, useState } from 'react'
import { StoreContext } from '../../../../context/StoreContext'
import "./ListOrder.css"
import { toast, ToastContainer } from 'react-toastify'
import { images } from '../../../../assets/images'
import NotFound from "../../../../components/NotFound/NotFound"
import axios from 'axios'

const ListOrder = () => {
    const { adminOrders, setAdminOrders, fetchAllOrder } = useContext(StoreContext);
    const [updatingOrderId, setUpdatingOrderId] = useState(null);
    const token = localStorage.getItem('token');

    useEffect(() => {
        fetchAllOrder();
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

    return (
        <div>
            <div className="order add">
                <div className=''>
                    <h3>Tìm kiếm đơn hàng</h3>
                </div>
                <h3>Các đơn đặt hàng</h3>
                {
                    adminOrders.length > 0
                        ? <div className="order-list">
                            {
                                adminOrders.map((order) => (
                                    <div className="order-item" key={order.id}>
                                        <img src={images.parcel_icon} alt="parcel_icon" />
                                        <div className="">
                                            <p className="order-item-tech">
                                                {
                                                    order.details.map((item, index) => {
                                                        if (index === order.details.length - 1) {
                                                            return item.product.name + " x " + item.quantity;
                                                        }
                                                        return item.product.name + " x " + item.quantity + ", ";
                                                    })
                                                }
                                            </p>
                                            <p className="order-item-name">{order.email}</p>
                                            <p className="order-item-address">{order.shippingAddress}</p>
                                            <p className="order-item-phone">{order.phone}</p>
                                        </div>
                                        <p>Items: {order.details.length}</p>
                                        <p>{VNDONG(order.totalAmount)}</p>
                                        <select
                                            onChange={(e) => {
                                                setUpdatingOrderId(order.id); // Đặt ID của đơn hàng đang được cập nhật
                                                updateOrderStatus(order.id, e.target.value);
                                            }}
                                            value={order.status}
                                            disabled={updatingOrderId === order.id} // Vô hiệu hóa trong khi cập nhật
                                        >
                                            <option value="paid">Paid</option>
                                            <option value="pending">Pending</option>
                                            <option value="deliveried">Deliveried</option>
                                        </select>
                                    </div>
                                ))
                            }
                        </div>
                        : <NotFound />
                }
            </div>
            <ToastContainer draggable stacked position='top-center' autoClose={1500} />
        </div>
    );
}

export default ListOrder;

// import React, { useContext, useEffect } from 'react'
// import { StoreContext } from '../../../../context/StoreContext'
// import "./ListOrder.css"
// import { toast, ToastContainer } from 'react-toastify'
// import { images } from '../../../../assets/images'
// import NotFound from "../../../../components/NotFound/NotFound"
// import axios from 'axios'
// const ListOrder = () => {

//     const { adminOrders, fetchAllOrder } = useContext(StoreContext);
//     const token = localStorage.getItem('token')
//     useEffect(() => {
//         // toast.promise(
//         //     fetchAllOrder(),
//         //     {
//         //         pending: "Loading orders ...",
//         //         success: "Loaded Successfully!",
//         //         error: "Error while loading orders data!!!"
//         //     }
//         // )
//         fetchAllOrder()
//     }, [adminOrders])

//     // const updateOrderStatus = async (orderId, status) => {
//     //     const params = new URLSearchParams();
//     //     params.append('status', status);
//     //     const res = await axios.put(`http://localhost:8080/admin/orders/${orderId}`, params, {
//     //         headers: {
//     //             Authorization: `Bearer ${token}`
//     //         }
//     //     })
//     //     if (res.data.statusCode === 200) {
//     //         console.log("Check res data >>> ", res.data.data);
//     //         fetchAllOrder()
//     //     }
//     // }
//     const updateOrderStatus = async (orderId, status) => {
//         const params = new URLSearchParams();
//         params.append('status', status);

//         try {
//             const res = await axios.put(
//                 `http://localhost:8080/admin/orders/${orderId}`,
//                 params,
//                 {
//                     headers: {
//                         Authorization: `Bearer ${token}`,
//                         'Content-Type': 'application/x-www-form-urlencoded' // Đặt Content-Type
//                     }
//                 }
//             );

//             if (res.status === 200 && res.data.statusCode === 200) {
//                 console.log("Check res data >>> ", res.data.data);
//                 fetchAllOrder(); // Gọi lại phương thức để cập nhật danh sách đơn hàng
//             } else {
//                 console.error("Failed to update order status:", res.data);
//             }
//         } catch (error) {
//             console.error("Error updating order status:", error);
//         }
//     }


//     const VNDONG = (number) => {
//         return number.toLocaleString('it-IT', { style: 'currency', currency: 'VND' });
//     };

//     return (
//         <div>
//             <div className="order add">
//                 <div className=''>
//                     <h3>Tìm kiếm đơn hàng</h3>
//                 </div>
//                 <h3>Các đơn đặt hàng</h3>
//                 {
//                     adminOrders
//                         ?
//                         <div className="order-list">
//                             {
//                                 adminOrders.map((order, index) => (
//                                     <div className="order-item" key={index}>
//                                         <img src={images.parcel_icon} alt="parcel_icon" />
//                                         <div className="">
//                                             <p className="order-item-tech">
//                                                 {
//                                                     order.details.map((item, index) => {

//                                                         if (index === order.details.length - 1) {
//                                                             return item.product.name + " x " + item.quantity
//                                                         }
//                                                         return item.product.name + " x " + item.quantity + ", "
//                                                     })
//                                                 }
//                                             </p>
//                                             <p className="order-item-name">
//                                                 {order.email}
//                                             </p>
//                                             <p className="order-item-address">
//                                                 <p>{order.shippingAddress}</p>
//                                             </p>
//                                             <p className="order-item-phone">
//                                                 {order.phone}
//                                             </p>
//                                         </div>
//                                         <p>Items: {order.details.length}</p>
//                                         <p>{VNDONG(order.totalAmount)}</p>
//                                         <select onChange={(e) => {
//                                             toast.promise(updateOrderStatus(order.id, e.target.value),
//                                                 {
//                                                     success: "Updated order",
//                                                     pending: "Updating order ...",
//                                                     error: "Error while updating order"
//                                                 })
//                                         }} value={order.status}>
//                                             <option value="paid">Paid</option>
//                                             <option value="pending">Pending</option>
//                                             <option value="deliveried">Deliveried</option>
//                                         </select>
//                                     </div>
//                                 ))
//                             }
//                         </div>
//                         :
//                         <NotFound />
//                 }
//             </div>
//             <ToastContainer draggable stacked position='top-center' autoClose={1500} />
//         </div >
//     )
// }

// export default ListOrder