import React, { useContext, useEffect, useState } from 'react'
import Popup from 'reactjs-popup';
import "./MyOrders.css"
import { StoreContext } from '../../context/StoreContext'
import axios from 'axios'
import { ToastContainer, toast } from 'react-toastify'
import { images } from '../../assets/images'
import OrderDetail from '../../components/OrderDetail/OrderDetail';
import { useLocation } from 'react-router-dom';
import ReactPaginate from 'react-paginate';
import NotFound from '../../components/NotFound/NotFound';

const MyOrders = () => {

    const [ordersSize, setOrdersSize] = useState([])
    const location = useLocation();
    const pageCount = Math.ceil(ordersSize / 10);
    const queryParams = new URLSearchParams(location.search);
    const currentPage = parseInt(queryParams.get('page')) || 1;
    const BASE_URL = "http://localhost:8080"
    const { profileInfo, userOrders, setUserOrders } = useContext(StoreContext)

    const fetchOrders = async () => {
        try {
            const res = await axios.get(`${BASE_URL}/api/v1/my_orders/${profileInfo.id}?page=${currentPage}`)
            if (res.data.statusCode === 200) {
                console.log(res);
                setUserOrders(res.data.dataList)
                setOrdersSize(prev => res.data.amountAllData)
            } else {
                throw new Error(res.data.message)
            }
        } catch (e) {
            toast.error(e.message)
        }
    }

    const VNDONG = (number) => {
        return number.toLocaleString('it-IT', { style: 'currency', currency: 'VND' });
    };

    const handleCancelOrder = async (id) => {
        try {
            const res = await axios.put(`http://localhost:8080/api/v1/orders/${id}?status=canceled`)
            if (res.data.statusCode === 200) {
                await fetchOrders()
            } else {
                throw new Error(res.data.message)
            }
        } catch (e) {
            toast.error(e.message)
        }
    }

    useEffect(() => {
        if (profileInfo.id) {
            if (userOrders.length === 0) {

                toast.promise(
                    fetchOrders(),
                    {
                        error: "Error occuring while loading orders data"
                    })
            }
        }

    }, [profileInfo])

    return (
        <div className='my-orders'>
            <h2>Đơn hàng của tôi</h2>
            <div className="container">
                {
                    userOrders?.length > 0 ?
                        userOrders?.map((order, index) => {
                            return (
                                <div key={index} className="my-orders-order">
                                    <img src={images.parcel_icon} alt="parcel_icon" />
                                    <b>Mã đơn hàng: {order.id}</b>
                                    <p>
                                        {
                                            order?.details?.map((item, index) => {
                                                if (index === order?.details?.length - 1) {
                                                    return item?.product?.name + ' x ' + item?.quantity
                                                } else {
                                                    return item?.product?.name + ' x ' + item?.quantity + ", "
                                                }
                                            })
                                        }
                                    </p>
                                    <p>Tổng tiền: {VNDONG(order.totalAmount)}</p>
                                    <p>Vật phẩm: {order.details.length}</p>
                                    <p>
                                        <span>&#x25cf;</span>
                                        <b>
                                            {
                                                order.status === "pending" && "Đang xử lý"
                                            }
                                            {
                                                order.status === "processed" && "Đã xử lý"
                                            }
                                            {
                                                order.status === "paid" && "Đã thanh toán"
                                            }
                                            {
                                                order.status === "delivered" && "Đã giao"
                                            }
                                            {
                                                order.status === "canceled" && "Đã hủy"
                                            }
                                        </b>
                                    </p>
                                    {/* <button onClick={() => fetchOrders()}>Làm mới</button> */}
                                    <Popup
                                        trigger={
                                            <button onClick={() => console.log(order)}>Chi tiết</button>
                                        }
                                        modal
                                        nested
                                    >
                                        {
                                            close => <OrderDetail close={close} order={order} />
                                        }
                                    </Popup>
                                    {
                                        order.status === "processed" || order.status === "canceled" ?
                                            <button style={{ opacity: "0.7" }} disabled >Hủy</button>
                                            :
                                            <button onClick={() => handleCancelOrder(order.id)} >Hủy</button>
                                    }
                                </div>
                            )
                        })
                        :
                        <NotFound />
                }
            </div>
            {/* pagination */}
            <ReactPaginate
                className='pagination_order'
                breakLabel="..."
                nextLabel=">>"
                onPageChange={(e) => window.location.href = `/myorder?page=${e.selected + 1}`}
                pageRangeDisplayed={5}
                pageCount={pageCount}
                previousLabel="<<"
                renderOnZeroPageCount={null}
                forcePage={currentPage - 1}
            />


            <ToastContainer position="top-center" autoClose={1500} draggable stacked hideProgressBar />
        </div>
    )
}

export default MyOrders