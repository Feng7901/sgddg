package fuxi;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class yi {
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // 连接到 MySQL 数据库
            String url = "jdbc:mysql://localhost:3306/fuxianl";
            String username = "root";
            String password = "123456";
            conn = DriverManager.getConnection(url, username, password);

            // 获取部门列表
            Map<Integer, String> departmentMap = new HashMap<>();
            System.out.println("部门列表：");
            String query = "SELECT * FROM department";
            stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int deptId = rs.getInt("did");
                String deptName = rs.getString("dname");
                departmentMap.put(deptId, deptName);
                System.out.println(deptId + "：" + deptName);
            }

            // 添加默认部门ID和名称
            departmentMap.put(1, "市场部");
            departmentMap.put(2, "研发部");
            departmentMap.put(3, "总裁办公室");

            // 获取用户输入的员工信息
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("请输入员工姓名：");
            String ename = reader.readLine();
            System.out.print("请输入员工性别（1代表男，2代表女）：");
            String esex = reader.readLine();
            while (!esex.equals("1") && !esex.equals("2")) { // 验证性别
                System.out.print("请输入正确的性别（1代表男，2代表女）：");
                esex = reader.readLine();
            }
            System.out.print("请输入员工生日（YYYY-MM-DD）：");
            String ebirthday = reader.readLine();
            System.out.print("请输入员工入职时间（YYYY-MM-DD）：");
            String ehiredate = reader.readLine();
            int d_id = -1; // 初始化部门ID
            while (true) {
                System.out.print("请输入员工所在部门 ID：");
                String input = reader.readLine();
                try {
                    d_id = Integer.parseInt(input);
                    if (d_id <= 3) { // 验证部门ID
                        break;
                    } else {
                        System.out.println("请输入正确的部门 ID！");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("请输入正确的部门 ID！");
                }
            }

            // 插入员工记录
            String insertQuery = "INSERT INTO employee (ename, esex, ebirthday, ehiredate, d_id) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, ename);
            stmt.setString(2, (esex.equals("1") ? "男" : "女"));
            stmt.setString(3, ebirthday);
            stmt.setString(4, ehiredate);
            stmt.setInt(5, d_id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("成功插入员工记录。员工信息如下：");
                System.out.println("姓名：" + ename);
                System.out.println("性别：" + (esex.equals("1") ? "男" : "女"));
                System.out.println("生日：" + ebirthday);
                System.out.println("入职时间：" + ehiredate);
                System.out.println("所在部门：" + departmentMap.get(d_id));
                System.out.println("录入成功！！！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}