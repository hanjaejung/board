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
import { Link, useLocation } from 'react-router-dom';

// @mui material components
import Grid from "@mui/material/Grid";
import Card from "@mui/material/Card";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDAlert from "components/MDAlert";
import MDButton from "components/MDButton";
import MDSnackbar from "components/MDSnackbar";

// Material Dashboard 2 React example components
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import DashboardNavbar from "examples/Navbars/DashboardNavbar";
import Footer from "examples/Footer";

function BoardDetail() {
  const { state } = useLocation();
  console.log(state);
  const [page, setPage] = useState(0);
  const [title, setTitle] = useState(state.title);
  const [writer, setWriter] = useState(state.user.userName);
  const [body, setBody] = useState(state.body);
  const [id, setId] = useState(state.id);

  const [likes, setLikes] = useState(0);
  const [comments, setComments] = useState([]);
  const [totalPage, setTotalPage] = useState(0);
  const [comment, setComment] = useState();

  return (
    <DashboardLayout>
      <DashboardNavbar />
      <MDBox pt={6} pb={3}>
        <Card>
          <MDBox pt={4} pb={3} px={3}>
            <Grid container>
              <Grid item xs={6}>
                <MDTypography fontWeight="bold" variant="body2">
                  {title}
                </MDTypography>
              </Grid>
              <Grid item xs={6}>
                <MDTypography variant="body2" textAlign="right">
                  {writer}
                </MDTypography>
              </Grid>
            </Grid>
            <MDTypography variant="body2">{body}</MDTypography>
          </MDBox>
        </Card>
      </MDBox>
    </DashboardLayout>
  );
}

export default BoardDetail;
