package Server;

import Client.DataTypes.Matrix;
import Client.DataTypes.Point;
import Client.Login.LoginModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UpdateData {
    private Queries queries;
    private LoginModel loginModel;

    private UpdateData() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        queries = new Queries();
        ResultSet patients= queries.getAllPatients();
        while (patients.next()){
            ResultSet dataTable = queries.getPatientDataTable(patients.getString(1));
            dataTable.next();
            dataTable.absolute(1);
            while (dataTable.absolute(dataTable.getRow())){
                Calendar calendar = Calendar.getInstance();
                int year = Integer.parseInt(dataTable.getString("year"));
                for (int i = 11;i<12;i++) {
                    calendar.set(year, i, 1);
                    String month = calendar.getDisplayName(2, 2, Locale.US);
                    String stringValues = dataTable.getString(month);
                    ArrayList<Client.DataTypes.Point> points = new ArrayList<>();
                    if (stringValues != null){
                        ArrayList<String> values = new ArrayList<>(Arrays.asList(stringValues.split(",")));
                        if (dataTable.getString("test").equals("T") || dataTable.getString("test").equals("G")){
                            //values.remove(values.size() - 2);
                            for (String string:values){
                                String value[] = string.split(" ");
                                points.add(new Point(Integer.valueOf(value[1]), Integer.valueOf(value[0])));
                            }
                        }
                        String function = buildLagrangeFunction(points);
                    }
                }
                dataTable.absolute(dataTable.getRow() + 1);
            }
        }

    }

    private String buildLagrangeFunction(ArrayList<Point> points) {
        int length = points.size();
        int x[] = new int[length];
        int y[] = new int[length];
        for (int i = 0;i<length;i++){
            x[i] = points.get(i).getIx();
            y[i] = points.get(i).getIy();
        }
        System.out.println(Arrays.toString(x) + "\n\n\n" + Arrays.toString(y) + "\n\n\n");
//        int newY[] = new int[y.length - 1];
//        length = 1;
//        for (int i = 1;i<y.length;i++){
//            newY[i - 1] = y[i] - y[i - 1];
//        }
//        int newYY[] = newY;
//        while (!Equals(newY) && length<y.length){
//            newY = new int[newYY.length - 1];
//            for (int i = 1;i<newYY.length;i++){
//                newY[i - 1] = newYY[i] - newYY[i - 1];
//                newYY = newY;
//            }
//            length++;
//        }
//        System.out.println(length + "  " + newY[0]);
//        System.exit(3);
        long matrix[][] = new long[length + 1][length + 1];
        for (int i = 0;i<length;i++){
            for (int z = 0;z<length;z++){
                matrix[i][z] = (long)Math.pow(x[i], length - (z + 1));
            }
            matrix[i][length] = y[i];
        }
        for (int i = 0;i<length;i++)
            System.out.println(Arrays.toString(matrix[i]));
        //System.exit(1);
        Matrix matrix1 = new Matrix(matrix);
        matrix1.RREF();
        System.out.println("\n\n\n" + matrix1.toString());
        System.exit(2);

        return "";

    }

    private boolean Equals(int[] newY) {
        for (int i = 0;i<newY.length - 1;i++)
            if (newY[i + 1] != newY[i]) {
                System.out.println(i + "  "  + (i+1));
                return false;
            }
        return true;
    }


    public static void main(String args[]) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        new UpdateData();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.NOVEMBER, 25, 20, 12);
        String s = "37.8";
        double x = 37.8;
        x = Double.valueOf(s);
        System.out.println(x);
    }
}
