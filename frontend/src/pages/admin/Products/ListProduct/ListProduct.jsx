import React, { useContext, useEffect } from 'react'
import axios from 'axios'
import { useNavigate } from 'react-router-dom'
import "./ListProduct.css"
import { ToastContainer, toast } from 'react-toastify'
import NotFound from '../../../../components/NotFound/NotFound'
import { StoreContext } from '../../../../context/StoreContext'
const ListProduct = () => {

    const BASE_URL = "http://localhost:8080"
    const navigate = useNavigate()
    const token = localStorage.getItem('token')
    const { cateAdminProducts, setCateAdminProducts, setAdminProducts, adminProducts, fetchAdminProductsByCategory } = useContext(StoreContext)

    useEffect(() => {
        // if (adminProducts.length === 0) {
        //     toast.promise(fetchAdminProductsByCategory(), {
        //         success: "Loaded Successfully!",
        //         pending: "Loading products data!",
        //         error: "Error while loading products data!!!"
        //     }, {
        //         containerId: 'A'
        //     })
        // }
        fetchAdminProductsByCategory()

    }, [])

    const hidingProduct = async (e, id, isHide) => {
        // alert(`Deleted >>> ${id}`)
        const response = await axios.put(`${BASE_URL}/admin/products/setHide/${id}?isHide=${isHide}`, {}, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        })
        if (response.data.statusCode === 200) {
            // toast(response.data.message);
            await fetchAdminProductsByCategory()
            // setProducts(prevProducts => prevProducts.filter(product => product.id !== id));
        } else if (response.data.statusCode === 404) {
            toast(response.data.message)
        } else {
            toast(response.data.message)
        }
    }

    return (
        <>
            <div className='list add flex-col'>
                <p>All {cateAdminProducts}s</p>
                <div className="list-table">
                    <div className="list-table-format title">
                        <b>Image</b>
                        <b>Name</b>
                        <b>Category</b>
                        <b>Price</b>
                        <b>Action</b>
                    </div>
                    {
                        adminProducts.length > 0
                            ?
                            adminProducts.map((item, index) => {

                                return (
                                    <div className="list-table-format" key={index}>
                                        <img src={`/images/${item.image}`} alt="product_image" />
                                        <p>{item.name}</p>
                                        <p>{item.category.name}</p>
                                        <p>{item.price}</p>

                                        {
                                            item.hide === false ?
                                                <button className='cursor'
                                                    onClick={(e) => {
                                                        e.preventDefault()
                                                        if (window.confirm("Bạn chắc chắn muốn ẩn sản phẩm này?")) {
                                                            toast.promise(
                                                                hidingProduct(e, item.id, true),
                                                                {
                                                                    pending: 'Đang xử lý',
                                                                    success: 'Đã xử lý thành công 👌',
                                                                    error: 'Có lỗi khi xử lý 🤯'
                                                                },
                                                                { containerId: 'B' }
                                                            )
                                                        }
                                                    }}>Ẩn</button>
                                                :
                                                <button className='cursor'
                                                    onClick={(e) => {
                                                        e.preventDefault()
                                                        if (window.confirm("Delete this product?")) {
                                                            toast.promise(
                                                                hidingProduct(e, item.id, false),
                                                                {
                                                                    pending: 'Đang xử lý',
                                                                    success: 'Đã xử lý thành công 👌',
                                                                    error: 'Có lỗi khi xử lý 🤯'
                                                                },
                                                                { containerId: 'B' }
                                                            )
                                                        }
                                                    }}>Hủy ẩn</button>
                                        }

                                        <button onClick={() => navigate(`/products/${item.id}`)}>
                                            Chi tiết
                                        </button>

                                        <button className='cursor' onClick={() => navigate(`/admin/products/edit/${item.id}`)}>
                                            Chỉnh sửa
                                        </button>
                                    </div>
                                )
                            })
                            :
                            <NotFound />
                    }
                </div>
            </div>
            <ToastContainer position="top-center" containerId="A" stacked draggable hideProgressBar />
            <ToastContainer containerId="B" stacked draggable hideProgressBar />
        </>
    )
}

export default ListProduct