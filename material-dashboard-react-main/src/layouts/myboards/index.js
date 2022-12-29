/**
=========================================================
* Material Dashboard 2 React - v2.1.0
=========================================================

* Product Page: https://www.creative-tim.com/product/material-dashboard-react
* Copyright 2022 Creative Tim (https://www.creative-tim.com)

Coded by www.creative-tim.com

 =========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
*/

// @mui material components
import Grid from "@mui/material/Grid";
import Divider from "@mui/material/Divider";

// @mui icons
import FacebookIcon from "@mui/icons-material/Facebook";
import TwitterIcon from "@mui/icons-material/Twitter";
import InstagramIcon from "@mui/icons-material/Instagram";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";

// Material Dashboard 2 React example components
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import DashboardNavbar from "examples/Navbars/DashboardNavbar";
import Footer from "examples/Footer";
import ProfileInfoCard from "examples/Cards/InfoCards/ProfileInfoCard";
import ProfilesList from "examples/Lists/ProfilesList";
import DefaultProjectCard from "examples/Cards/ProjectCards/DefaultProjectCard";

// Overview page components
import Header from "layouts/myboards/components/Header";
import PlatformSettings from "layouts/myboards/components/PlatformSettings";

// Data
import profilesListData from "layouts/myboards/data/profilesListData";

// Images
import homeDecor1 from "assets/images/home-decor-1.jpg";
import homeDecor2 from "assets/images/home-decor-2.jpg";
import homeDecor3 from "assets/images/home-decor-3.jpg";
import homeDecor4 from "assets/images/home-decor-4.jpeg";
import team1 from "assets/images/team-1.jpg";
import team2 from "assets/images/team-2.jpg";
import team3 from "assets/images/team-3.jpg";
import team4 from "assets/images/team-4.jpg";
import {useEffect, useState} from "react";
import * as React from "react";
import {useNavigate} from "react-router";
import axios from "axios";
import Card from "@mui/material/Card";
import Button from "@mui/material/Button";
import MDPagination from "../../components/MDPagination";
import KeyboardArrowLeftIcon from "@mui/icons-material/KeyboardArrowLeft";
import KeyboardArrowRightIcon from "@mui/icons-material/KeyboardArrowRight";

function MyBoard() {

    const [page, setPage] = useState(0);
    const [render, setRender] = useState(false);
    const [tables, setTables] = useState([]);
    const [totalPage, setTotalPage] = useState(0);

    const [title, setTitle] = useState('');
    const [body, setBody] = useState('');
    const [open, setOpen] = React.useState(false);
    const [dialogTitle, setDialogTitle] = React.useState('');
    const [dialogMessage, setDialogMessage] = React.useState('');
    const navigate = useNavigate();

    const handleModify = (table) => {
        console.log('handleDetail');
        console.log(table);
        navigate('/board-modify', { state: table });
    };

    const handleDelete = (id) => {
        console.log('handleDelete id : ' + id);
        axios({
            url: '/api/board/' + id,
            method: 'DELETE',
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('token'),
            },
        })
            .then((res) => {
                console.log('success');
                console.log(res);
                console.log(page);
                handleGetTables(page);
            })
            .catch((error) => {
                console.log(error);
                navigate('/authentication/sign-in');
            });
    };

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const changePage = (pageNum) => {
        console.log('change pages');
        console.log(pageNum);
        console.log(page);
        setPage(pageNum);
        handleGetTables(pageNum);
    };

    const handleGetTables = (pageNum, event) => {
        console.log('handleGetPosts');
        axios({
            url: '/api/board/my?size=5&sort=id&page=' + pageNum,
            method: 'GET',
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('token'),
            },
        })
            .then((res) => {
                console.log('success');
                console.log(res);
                setTables(res.data.result.content);
                setTotalPage(res.data.result.totalPages);
            })
            .catch((error) => {
                console.log(error);
                navigate('/authentication/sign-in');
            });
    };

    useEffect(() => {
        handleGetTables();
    }, []);

    return (
        <DashboardLayout>
            <DashboardNavbar />
            <MDBox pt={3} pb={3}>
                {tables.map((table) => (
                    <MDBox pt={2} pb={2} px={3}>
                        <Card>
                            <MDBox pt={2} pb={2} px={2}>
                                <Grid container>
                                    <Grid item xs={6}>
                                        <MDTypography fontWeight="bold" variant="body2">
                                            {table.title}
                                        </MDTypography>
                                    </Grid>
                                    <Grid item xs={6}>
                                        <MDTypography variant="body2" textAlign="right">
                                            {table.user.userName}
                                        </MDTypography>
                                    </Grid>
                                </Grid>
                                <MDTypography variant="body2">{table.body}</MDTypography>
                                <Grid container>
                                    <Grid item xs={9}></Grid>
                                    <Grid item xs={1}>
                                        <Button onClick={() => handleModify(table)}>Modify</Button>
                                    </Grid>
                                    <Grid item xs={1}>
                                        <Button onClick={() => handleDelete(table.id)}>Delete</Button>
                                    </Grid>
                                </Grid>
                            </MDBox>
                        </Card>
                    </MDBox>
                ))}
            </MDBox>

            <MDPagination>
                <MDPagination item>
                    <KeyboardArrowLeftIcon></KeyboardArrowLeftIcon>
                </MDPagination>
                {[...Array(totalPage).keys()].map((i) => (
                    <MDPagination item onClick={() => changePage(i)}>
                        {i + 1}
                    </MDPagination>
                ))}
                <MDPagination item>
                    <KeyboardArrowRightIcon></KeyboardArrowRightIcon>
                </MDPagination>
            </MDPagination>
        </DashboardLayout>
    );
}

export default MyBoard;
