import javax.mail.Session; // Gmail'i kullanmak için Google Hesap'tan uygulama şifresi almak gerekiyor
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main
{
    public static class MailIslemleri
    {
        public void MailYolla(String baslik, String icerik, String hangiFile)
        {
            String kimden = "aicengiz13@gmail.com"; // Gönderen kişinin email'i
            String uygulamaSifresi ="hikr hpkx nldm cjwn"; // Gmail uygulama şifresi
            String host = "smtp.gmail.com"; // Email'i gmail smtp'si üzerinden atıcaz

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", host); // Mail sunucusu
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");


            Session session = Session.getInstance(properties, new javax.mail.Authenticator()
            {
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(kimden, uygulamaSifresi); // Hesap mail ve şifresi
                }
            });

            try {
                MimeMessage mesaj = new MimeMessage(session);
                mesaj.setFrom(new InternetAddress(kimden));

                Scanner scanner = new Scanner(new File(hangiFile)); // Dosyayı okuma

                scanner.nextLine(); // Başlığı atlamak için yazdım
                while (scanner.hasNextLine())
                {
                    String line = scanner.nextLine();

                    String [] parts = line.split("    "); // Bir satır kelimeyi TAB ile ayırdım
                    String email = parts[parts.length - 1]; // Bir satır kelimede son kısımdakini (yani email'i) aldım
                    mesaj.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                }

                mesaj.setSubject(baslik); // Email başlığı
                mesaj.setText(icerik); // Email mesajı

                Transport.send(mesaj);
            } catch (FileNotFoundException | MessagingException e) {
                e.printStackTrace();
            }
        }


    }

    public static class DosyaIslemleri
    {
        // Bu methodda Kullanıcılar.txt'yi TümKullanıcı.txt diye ayırıyorum (düzgün çalışmıyodu yoksa)
        public static void DosyaBol(String tumKul, String kul) throws IOException
        {
            PrintWriter yazma = new PrintWriter(tumKul);

            // Sadece emaillerin olduğu dosyayı yazmayı sağlıyor burası
            // Mesela "Bana örnek@gmail.com üzerinden ulaşabilirsiniz." diye bir sıra varsa sadece email'i okuyor
            Pattern pattern=Pattern.compile( "[a-zA-Z0-9]" + "[a-zA-Z0-9_.]" + "*@[a-zA-Z0-9]" + "+([.][a-zA-Z]+)+");
            BufferedReader buffoku = new BufferedReader(new FileReader(kul));

            String satir = buffoku.readLine();
            yazma.write("#TÜM ÜYELER" + "\n");
            while (satir != null)
            {
                Matcher matcher = pattern.matcher(satir); // Pattern'a göre Matcher satiri eşitlemeye çalışıyor
                while (matcher.find())
                {
                    yazma.println(matcher.group());
                }
                satir = buffoku.readLine();
            }
            yazma.flush();
        }

        // Burda ilk başta bölünmüş olan dosyaları en son birleştiriyorum ki tek bir dosyada bulunsun
        public static void DosyaBirlestir(String finalFile, String file2, String file3) throws IOException {
            PrintWriter yazici = new PrintWriter(finalFile);

            BufferedReader okuyucu = new BufferedReader(new FileReader(file2)); // file2 oku

            String satir = okuyucu.readLine(); // satir = her yeni satır

            // Döngüyle her satırı kopyalama
            while (satir != null)
            {
                yazici.println(satir);
                satir = okuyucu.readLine();
            }

            okuyucu = new BufferedReader(new FileReader(file3)); // file3 oku

            satir = okuyucu.readLine(); // satir = her yeni satır

            // Döngüyle her satırı kopyalama
            while(satir != null)
            {
                yazici.println(satir);
                satir = okuyucu.readLine();
            }
            yazici.flush();

            // Okuma yazma kapatma
            okuyucu.close();
            yazici.close();
        }
    }

    class UyeEkle
    {
        static String bosluk = "    ";
    }

    class GenelUye extends UyeEkle
    {
        public static void GenelUyeEkle(String isim, String soyisim, String email) throws IOException {
            File dosyaekle = new File("GenelKullanıcı.txt");
            FileWriter yaz = new FileWriter(dosyaekle, true); // Zaten olan dosyaya append
            yaz.write( "\n" + isim + bosluk + soyisim + bosluk + email + "\n"); // Aralara TAB karakteri koydum
            yaz.flush();
            yaz.close();
        }
    }

    class ElitUye extends UyeEkle
    {
        public static void ElitUyeEkle(String isim, String soyisim, String email) throws IOException {
            File dosyaekle = new File("ElitKullanıcı.txt");
            FileWriter yaz = new FileWriter(dosyaekle, true); // Olan dosyaya append
            yaz.write("\n" + isim + bosluk + soyisim + bosluk + email + "\n"); // Aralara TAB karakteri koydum
            yaz.flush();
            yaz.close();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("1- Elit üye ekleme");
        System.out.println("2- Genel Üye ekleme");
        System.out.println("3- Mail Gönderme");

        Scanner scanner = new Scanner(System.in);
        int secim = scanner.nextInt(); // Seçim yaptım
        scanner.nextLine(); // "\n" karakteri gitsin diye bir daha next kullandım (bazen sorun yapıyor)

        switch (secim)
        {
            case 1:
                System.out.println("İsim giriniz: ");
                String isim1 = scanner.next();
                System.out.println("Soyisim giriniz: ");
                String soyisim1 = scanner.next();
                System.out.println("Email giriniz: ");
                String email1 = scanner.next();

                ElitUye.ElitUyeEkle(isim1, soyisim1, email1);
                System.out.println("Elit üye eklendi.");
                break;

            case 2:
                System.out.println("İsim giriniz: ");
                String isim2 = scanner.next();
                System.out.println("Soyisim giriniz: ");
                String soyisim2 = scanner.next();
                System.out.println("Email giriniz: ");
                String email2 = scanner.next();

                GenelUye.GenelUyeEkle(isim2, soyisim2, email2);
                System.out.println("Genel üye eklendi.");
                break;

            case 3:
                System.out.println("1- Elit üyelere mail");
                System.out.println("2- Genel üyelere mail");
                System.out.println("3- Tüm üyelere mail");

                MailIslemleri islemleri = new MailIslemleri();

                int menu2 = scanner.nextInt(); // İkinci seçimi kullanıcıdan aldım
                scanner.nextLine(); // "\n" karakterini aldım burda

                switch (menu2)
                {
                    case 1:
                        System.out.println("Mail başlığı ne olsun?");
                        String baslik1 = scanner.nextLine();
                        System.out.println("Mail içeriği ne olsun?");
                        String icerik1 = scanner.nextLine();
                        String hangiFile1 = "ElitKullanıcı.txt";

                        islemleri.MailYolla(baslik1, icerik1, hangiFile1);
                        break;

                    case 2:
                        System.out.println("Mail başlığı ne olsun?");
                        String baslik2 = scanner.nextLine();
                        System.out.println("Mail içeriği ne olsun?");
                        String icerik2 = scanner.nextLine();
                        String hangiFile2 = "GenelKullanıcı.txt";

                        islemleri.MailYolla(baslik2, icerik2, hangiFile2);
                        break;

                    case 3:
                        System.out.println("Mail başlığı ne olsun?");
                        String baslik3 = scanner.nextLine();
                        System.out.println("Mail içeriği ne olsun?");
                        String icerik3 = scanner.nextLine();
                        String hangiFile3 = "TümKullanıcı.txt";
                        String temp = "Kullanıcılar.txt";

                        DosyaIslemleri.DosyaBol(hangiFile3, temp);
                        islemleri.MailYolla(baslik3, icerik3, hangiFile3);
                        break;
                }
                break;

            default:
                System.out.println("1, 2 veya 3 seçilmedi.");
        }

        String finalFile = "Kullanıcılar.txt";
        String file2 = "ElitKullanıcı.txt";
        String file3 = "GenelKullanıcı.txt";

        DosyaIslemleri.DosyaBirlestir(finalFile,file2, file3);
        // Burda son defa nolur ne olmaz diye dosyaları birleştiriyorum
    }
}
