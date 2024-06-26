import React, { useContext, useEffect, useState } from 'react'
import "./MyOrders.css"
import { StoreContext } from '../../context/StoreContext'
import axios from 'axios'
import { ToastContainer, toast } from 'react-toastify'
import { images } from '../../assets/images'
const MyOrders = () => {

    const BASE_URL = "http://localhost:8080"
    // const [data, setData] = useState([])
    const { profileInfo, userOrders, setUserOrders } = useContext(StoreContext)

    const page = 1;
    const size = 10;

    const fetchOrders = async () => {
        const orders = await axios.get(`${BASE_URL}/api/v1/my_orders/${profileInfo.id}`)
        if (orders.data) {
            // console.log("Check log data >>>> ", orders.data.dataList);
            setUserOrders(orders.data.dataList)
        }
    }

    const VNDONG = (number) => {
        return number.toLocaleString('it-IT', { style: 'currency', currency: 'VND' });
    };

    useEffect(() => {
        if (profileInfo.id) {
            if (userOrders.length === 0)
                toast.promise(
                    fetchOrders(),
                    {
                        error: "Error occuring while loading orders data",
                        success: "Loaded your orders",
                        pending: "Loading your orders data"
                    })
        }
    }, [profileInfo])

    return (
        <div className='my-orders'>
            <h2>My Orders</h2>
            <div className="container">
                {
                    userOrders.map((order, index) => {
                        return (
                            <div key={index} className="my-orders-order">
                                <b>{order.id}</b>
                                <img src={images.parcel_icon} alt="parcel_icon" />
                                <p>
                                    {
                                        order.details.map((item, index) => {
                                            if (index === order.details.length - 1) {
                                                return item.product.name + ' x ' + item.quantity
                                            } else {
                                                return item.product.name + ' x ' + item.quantity + ", "
                                            }
                                        })
                                    }
                                </p>
                                <p>{VNDONG(order.totalAmount)}</p>
                                <p>Items: {order.details.length}</p>
                                <p><span>&#x25cf;</span> <b> {order.status}</b></p>
                                <button onClick={() => fetchOrders()}>Track Order</button>
                            </div>
                        )
                    })
                }
            </div>
            <ToastContainer position="top-center" autoClose={1500} draggable stacked hideProgressBar />
        </div>
    )
}

export default MyOrders