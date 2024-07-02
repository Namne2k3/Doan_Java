import React, { useContext, useEffect, useState } from 'react'
import "./home.css"
import Header from '../../components/Header/Header'
import Menu from '../../components/Menu/Menu'
import TechDisplay from '../../components/TechDisplay/TechDisplay'
import { fetchALlCategories } from '../../services/CategoryService'
import axios from 'axios'
import { StoreContext } from '../../context/StoreContext'

const Home = () => {

    const [category, setCategory] = useState("")
    const { fetchProductsByBrand, fetchProductsByCategory } = useContext(StoreContext)

    const [menuList, setMenuList] = useState([])
    const [brandList, setBrandList] = useState([])


    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const dataCategories = await fetchALlCategories();
                if (dataCategories) {
                    setMenuList(dataCategories);
                }
            } catch (error) {
                console.error('Error fetching categories:', error);
            }
        };

        const fetchBrandList = async () => {
            try {
                const res = await axios.get(`http://localhost:8080/api/v1/brands`);
                if (res.data.statusCode === 200) {
                    setBrandList(res.data.dataList);
                }
            } catch (error) {
                console.error('Error fetching categories:', error);
            }
        }

        fetchCategories()
        fetchBrandList()
        fetchProductsByCategory()
    }, []);

    const handleSetBrand = async (e) => {
        try {
            await fetchProductsByBrand(e.target.value)
        } catch (e) {
            console.log(e.message);
        }
    }

    const handleSetCategory = async (name) => {
        console.log(name);
        try {
            await fetchProductsByCategory(name)
        } catch (e) {
            console.log(e.message);
        }
    }

    return (
        <div>
            <Header />
            <Menu handleSetCategory={handleSetCategory} handleSetBrand={handleSetBrand} brandList={brandList} menuList={menuList} setCategory={setCategory} />
            <TechDisplay />
        </div>
    )
}

export default Home