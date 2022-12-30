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

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";

// Material Dashboard 2 React example components
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";

// Images
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
    const [tables, setTables] = useState([]);
    const [totalPage, setTotalPage] = useState(0);

    const [open, setOpen] = React.useState(false);
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
                                    <Grid item xs={10}></Grid>
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
