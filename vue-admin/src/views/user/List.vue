<template>
  <div class="user-list">
    <el-table :data="filteredUserList" style="width: 100%">
      <el-table-column label="用户 ID" width="430" prop="id"></el-table-column>
      <el-table-column label="用户名" width="200" prop="username"></el-table-column>
      <el-table-column label="昵称" width="200" prop="nickname"></el-table-column>
      <el-table-column label="邮箱" prop="email"></el-table-column>
      <el-table-column label="性别" prop="gender"></el-table-column>
      <el-table-column label="生日" prop="birthday"></el-table-column>
      <el-table-column label="个人简介" prop="info"></el-table-column>
      <el-table-column label="更新时间" prop="updateAt"></el-table-column>
      <el-table-column width="180" align="right">
        <template slot="header" slot-scope="scope">
          <el-input v-model="search" size="mini" placeholder="输入关键字搜索"/>
        </template>
        <template slot-scope="scope">
          <el-button size="mini" icon="el-icon-edit" type="primary" @click="handleEdit(scope.$index, scope.row)">修改</el-button>
          <el-popconfirm confirm-button-text='确定' cancel-button-text='不用了' icon="el-icon-info" icon-color="red" @confirm="handleDelete(scope.$index,scope.row)" title="确定要删除此用户吗？">
            <el-button style="margin-left: 8px" size="mini" icon="el-icon-delete" type="danger" slot="reference">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
        title="编辑用户信息"
        :visible.sync="dialogEditUser"
        width="50%">
      <el-form ref="form" :model="formUser" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="formUser.username"></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="formUser.nickname"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="formUser.email"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="formUser.gender" placeholder="请选择性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="生日">
          <el-date-picker type="date" value-format="yyyy年-MM月-dd日" placeholder="选择日期" v-model="formUser.birthday" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item label="个人简介">
          <el-input type="textarea" v-model="formUser.info"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
      <el-button @click="dialogEditUser = false">取消</el-button>
      <el-button type="primary" @click="submitUpdateUser()">确定</el-button>
    </span>
    </el-dialog>

    <el-pagination
        @current-change="handlePageChange"
        :current-page="currentPage"
        :page-sizes="[4, 8, 12]"
        :page-size="pageSize"
        layout="sizes, prev, pager, next, jumper"
        :total="userList.length">
    </el-pagination>
  </div>


</template>

<script>
import {FindAllUser, UpdateUser, DeleteUser, DeleteUserByUsername} from "@/api/user";

export default {
  data() {
    return {
      currentPage: 1, // 当前页码
      pageSize: 4, // 每页显示数量
      userList: [], // 用户列表
      search: '', // 搜索关键字
      dialogEditUser: false, // 对话框的显示状态
      formUser: {}, // 表单的数据模型
    }
  },

  computed: {
    filteredUserList() {
      const startIndex = (this.currentPage - 1) * this.pageSize; // 计算起始索引
      const endIndex = startIndex + this.pageSize; // 计算结束索引
      return this.userList
          .filter(user => !this.search || user.username.includes(this.search) || user.nickname.includes(this.search))
          .slice(startIndex, endIndex); // 过滤并截取当前页的数据
    },
  },

  mounted() {
    this.fetchUserList();
  },

  methods: {

    async handleEdit(index, row) {
      this.dialogEditUser = true;
      this.formUser = { ...row }; // 使用展开运算符复制用户对象
    },

    async submitUpdateUser() {
      try {
        await UpdateUser(this.formUser); // 调用API函数更新用户信息
        this.dialogEditUser = false; // 关闭对话框
        this.fetchUserList(); // 更新用户列表
        this.$message.success("用户信息更新成功");
      } catch (error) {
        console.error("Error updating user", error);
        this.$message.error("用户信息更新失败");
      }
    },
    async fetchUserList() {
      try {
        const response = await FindAllUser();
        this.userList = response.data;
      } catch (error) {
        console.error("Error fetching user list", error);
      }
    },



    async handleDelete(index, row) {
      try {
        await DeleteUserByUsername(row.username);
        this.userList.splice(index, 1);
        this.$message.success("用户删除成功");
      } catch (error) {
        console.error("Error deleting user", error);
        this.$message.error("用户删除失败");
      }
    },


    handlePageChange(page) {
      this.currentPage = page; // 更新当前页码
    },
  },
}
</script>

<style scoped>
.user-list {
  padding: 20px;
}
</style>


<style scoped>
.demo-table-expand {
  font-size: 0;
}

.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}

.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
</style>