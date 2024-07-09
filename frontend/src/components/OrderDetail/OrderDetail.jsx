import React from 'react'
import moment from 'moment'
import { ToastContainer, toast } from 'react-toastify'
import { images } from '../../assets/images'

const OrderDetail = ({ order, close }) => {
    const VNDONG = (number) => {
        return number.toLocaleString('it-IT', { style: 'currency', currency: 'VND' })
    }
    console.log(order);
    return (
        <>
            {
                order?.id &&
                <>
                    <div className='product-info-container'>
                        <div className='product-info-head'>
                            <div className='d-flex gap-4 text-center'>
                                <img src={images.parcel_icon} alt="parcel_icon" />
                                <h1>Chi tiết hóa đơn</h1>
                            </div>
                        </div>
                        <table className='product-info-table'>
                            <tbody>
                                <tr>
                                    <td>Mã hóa đơn:</td>
                                    <td>
                                        <b>{order.id}</b>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Tên người đặt:</td>
                                    <td>{order.user.username}</td>
                                </tr>
                                <tr>
                                    <td>Email người đặt:</td>
                                    <td>{order.email}</td>
                                </tr>
                                <tr>
                                    <td>SĐT người đặt:</td>
                                    <td>{order.phone}</td>
                                </tr>
                                <tr>
                                    <td>Địa chỉ:</td>
                                    <td>{order.shippingAddress}</td>
                                </tr>
                                <tr>
                                    <td>Sản phẩm:</td>
                                    <td>
                                        <ul style={{ margin: "0px" }}>
                                            {
                                                order.details.map((detail, index) => {
                                                    return (
                                                        <li key={index}>
                                                            <b>{detail.product.name}</b>
                                                        </li>
                                                    )
                                                })
                                            }
                                        </ul>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Tổng tiền hóa đơn:</td>
                                    <td>{VNDONG(order.totalAmount)}</td>
                                </tr>
                                <tr>
                                    <td>Trạng thái:</td>
                                    <td>
                                        {
                                            order.status === "pending"
                                                ? "Đang xử lý"
                                                : order.status === "processed" ? "Đã xử lý"
                                                    : order.status === "paid" ? "Đã thanh toán"
                                                        : order.status === "delivered" ? "Đã giao"
                                                            : order.status === "canceled" ? "Đã hủy"
                                                                : ""
                                        }
                                    </td>
                                </tr>
                                <tr>
                                    <td>Ngày đặt hàng:</td>
                                    <td>{moment(order.createdAt).format('DD-MM-YYYY HH:mm:ss')}</td>
                                </tr>
                                <tr>
                                    <td>Đã cập nhật đơn hàng vào:</td>
                                    <td>{moment(order.updatedAt).format('DD-MM-YYYY HH:mm:ss')}</td>
                                </tr>
                                <tr>
                                    <td>Phương thức thanh toán:</td>
                                    <td>
                                        {
                                            order.paymentMethod === "COD" ? "Thanh toán khi nhận hàng" : "Thanh toán qua ngân hàng"
                                        }
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <ToastContainer draggable stacked autoClose={2000} hideProgressBar />
                </>
            }
        </>
    )
}

export default OrderDetail