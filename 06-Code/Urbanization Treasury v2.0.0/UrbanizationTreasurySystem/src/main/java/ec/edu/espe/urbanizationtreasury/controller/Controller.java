package ec.edu.espe.urbanizationtreasury.controller;

//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import ec.edu.espe.urbanizationtreasury.model.Payment;
import ec.edu.espe.urbanizationtreasury.model.Resident;
import ec.edu.espe.urbanizationtreasury.view.FrmResidentInformation;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 *
 * @author Rivera Joel, WebMasterTeam, DCCO-ESPE
 */
public class Controller extends javax.swing.JFrame{

    private static final Scanner sc = new Scanner(System.in);
    private static final Resident resident = new Resident();
    private static final Payment payment = new Payment();
    
    
    public static void enterResident(MongoDatabase database) {

        String idValidation;
        int reEnterId;
        System.out.println("Insert the resident id: ");
        resident.setId(sc.nextLong());
        idValidation = String.valueOf(resident.getId());
        reEnterId = dniValidation(idValidation);

        if (reEnterId == 0) {
            while (reEnterId == 0) {
                System.out.println("Re-enter the DNI:");
                resident.setId(sc.nextLong());
                idValidation = String.valueOf(resident.getId());
                reEnterId = dniValidation(idValidation);
            }
        }
        sc.nextLine();
        System.out.println("Insert the resident name: ");
        resident.setName(sc.nextLine());
        System.out.println("Insert the resident batch number: ");
        resident.setBatch(sc.nextInt());
        sc.nextLine();

        MongoCollection<Document> collection = database.getCollection("Residents");
        Document inspection = new Document("_id", new ObjectId())
                .append("id", resident.getId())
                .append("name", resident.getName())
                .append("batch", resident.getBatch());

        collection.insertOne(inspection);
        System.out.println("|||User entered |||");

    }

    public static boolean noRepeatRecident(MongoDatabase database, Resident resident, boolean existResident) {

        MongoCollection<Document> collection2 = database.getCollection("Residents");
        Bson filter = Filters.and(Filters.all("id", resident.getId()));

        if (collection2.find(filter).first() == null) {
            existResident = false;
        }
        if (collection2.find(filter).first() != null) {
            existResident = true;
        }
        return existResident;

    }

    public static String readMongo(MongoDatabase database, Resident resident, String month) {
        
        String document = "";
        Gson gson = new Gson();
        
        MongoCollection<Document> collection = database.getCollection("Residents");
        
        Bson filter = Filters.eq("id", resident.getId());
        Document query = collection.find(Filters.and(filter)).first();

        document = query.toJson();
        TypeToken<Payment> type = new TypeToken<Payment>() {
        };
        Payment paymentType = gson.fromJson(document, type.getType());

        month = (paymentType.getMonth());
        
        return month;
        
    }

    public static int dniValidation(String dniValidation) {

        int number = 0, suma = 0, resultado = 0, result = 0;

        for (int i = 0; i < dniValidation.length(); i++) {
            number = Integer.parseInt(String.valueOf(dniValidation.charAt(i)));
            if (i % 2 == 0) {
                number = number * 2;
                if (number > 9) {
                    number = number - 9;
                }
            }
            suma = suma + number;
        }
        if (suma % 10 != 0) {
            resultado = 10 - (suma % 10);
            if (resultado == number) {
                System.out.println("Valid DNI");
            } else if (resultado != number) {
                System.out.println("Invalid DNI");
            }
        } else {
            System.out.println("Valid DNI");
            result = 1;
        }
        return (result);
    }

    public static void printHistory(ArrayList<Float> payments, ArrayList<String> mounthsOfPayments) {

        
    }
    
    public static void enterPayments(MongoDatabase database, String payType, Resident resident, Payment payment) {

        MongoCollection<Document> collection = database.getCollection(payType);
        
        System.out.println("---------------------------------");
        System.out.println("" + resident.getId());
        
        Document inspection = new Document("_id", new ObjectId())
                .append("id", resident.getId())
                .append("month", payment.getMonth())
                .append("payment", payment.getPayment());

        collection.insertOne(inspection);
    }

}
