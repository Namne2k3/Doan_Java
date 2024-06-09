import React, { useEffect, useState } from 'react'
import "./home.css"
import Header from '../../components/Header/Header'
import Menu from '../../components/Menu/Menu'
import TechDisplay from '../../components/TechDisplay/TechDisplay'
import { fetchALlCategories } from '../../services/CategoryService'
import { ToastContainer } from 'react-toastify'

const Home = () => {

    const [category, setCategory] = useState("")
    const [menuList, setMenuList] = useState([])

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

        fetchCategories();
    }, []);


    return (
        <div>
            <Header />
            <Menu menuList={menuList} category={category} setCategory={setCategory} />
            <TechDisplay category={category} />
        </div>
    )
}

export default Home