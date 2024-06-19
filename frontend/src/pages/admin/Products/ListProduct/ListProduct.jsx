import React, { useContext, useEffect, useState } from 'react'
import axios from 'axios'
import "./ListProduct.css"
import { ToastContainer, toast } from 'react-toastify'
import NotFound from '../../../../components/NotFound/NotFound'
import { StoreContext } from '../../../../context/StoreContext'
const ListProduct = () => {

    const BASE_URL = "http://localhost:8080"

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
    }, [adminProducts])

    const removeProduct = async (e, id) => {
        // alert(`Deleted >>> ${id}`)
        const response = await axios.delete(`${BASE_URL}/api/v1/products/${id}`)
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
                                    <a href={`/products/${item.id}`} className="list-table-format" key={index}>
                                        <img src={item.image} alt="product_image" />
                                        <p>{item.name}</p>
                                        <p>{item.category.name}</p>
                                        <p>{item.price}</p>

                                        <p className='cursor'
                                            onClick={(e) => {
                                                e.preventDefault()
                                                if (window.confirm("Delete this product?")) {
                                                    toast.promise(
                                                        removeProduct(e, item.id),
                                                        {
                                                            pending: 'Remove Product is pending',
                                                            success: 'Removed Product Successfully 👌',
                                                            error: 'Error rejected Remove product 🤯'
                                                        },
                                                        { containerId: 'B' }
                                                    )
                                                }
                                            }} onContextMenu={(e) => e.preventDefault()}>X</p>
                                    </a>
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