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

import * as React from 'react';
import { useState, useEffect } from 'react';
import {useNavigate} from 'react-router';

// @mui material components
import Grid from "@mui/material/Grid";
import Card from "@mui/material/Card";
import KeyboardArrowLeftIcon from '@mui/icons-material/KeyboardArrowLeft';
import KeyboardArrowRightIcon from '@mui/icons-material/KeyboardArrowRight';

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDPagination from 'components/MDPagination';

// Material Dashboard 2 React example components
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import Button from '@mui/material/Button';


// Data
import axios from 'axios';

function Tables() {

  const [page, setPage] = useState(0);
  const [tables, setTables] = useState([]);
  const [totalPage, setTotalPage] = useState(0);

  const [open, setOpen] = React.useState(false);
  const navigate = useNavigate();

  const handleDetail = (table) => {
    console.log('handleDetail');
    console.log(table);
    navigate('/board-detail', { state: table });
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
      url: '/api/board?size=5&sort=id&page=' + pageNum,
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
                <MDBox pt={2} pb={2} px={3}>
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
                    <Grid item xs={11}></Grid>
                    <Grid item xs={1}>
                      <Button onClick={() => handleDetail(table)}>Detail</Button>
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

export default Tables;
