using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using oblig1.Models;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace oblig1.DAL
{
    public class DbInit
    {
        public static void Initialize(IApplicationBuilder app)
        {
            using (var serviceScope = app.ApplicationServices.CreateScope())
            {
                var context = serviceScope.ServiceProvider.GetService<BussReiseContext>();

                context.Database.EnsureDeleted();
                context.Database.EnsureCreated();

                var type1 = new BillettType { Type = "voksen", Pris = 200 };
                var type2 = new BillettType { Type = "student", Pris = 100 };
                var type3 = new BillettType { Type = "barn", Pris = 75 };

                context.BillettTyper.Add(type1);
                context.BillettTyper.Add(type2);
                context.BillettTyper.Add(type3);


                List<Strekninger> steder = new List<Strekninger>();
                steder.Add(new Strekninger { StedNavn = "Oslo" });
                steder.Add(new Strekninger { StedNavn = "Bergen" });
                steder.Add(new Strekninger { StedNavn = "Stavanger" });
                steder.Add(new Strekninger { StedNavn = "Kristiansand" });
                steder.Add(new Strekninger { StedNavn = "Trondheim" });
                steder.Add(new Strekninger { StedNavn = "Fredrikstad" });
                

                var tid1 = new Tidsplaner { ReiseTid = "08:00", Ukedag = 1 };
                var tid2 = new Tidsplaner { ReiseTid = "12:00", Ukedag = 1 };
                var tid3 = new Tidsplaner { ReiseTid = "16:00", Ukedag = 1 };
                var tid4 = new Tidsplaner { ReiseTid = "20:00", Ukedag = 1 };
                var tid5 = new Tidsplaner { ReiseTid = "08:00", Ukedag = 2 };
                var tid6 = new Tidsplaner { ReiseTid = "12:00", Ukedag = 2 };
                var tid7 = new Tidsplaner { ReiseTid = "15:00", Ukedag = 2 };
                var tid8 = new Tidsplaner { ReiseTid = "08:00", Ukedag = 3 };
                var tid9 = new Tidsplaner { ReiseTid = "12:00", Ukedag = 3 };
                var tid10 = new Tidsplaner { ReiseTid = "16:00", Ukedag = 3 };
                var tid11 = new Tidsplaner { ReiseTid = "20:00", Ukedag = 3 };
                var tid12 = new Tidsplaner { ReiseTid = "08:00", Ukedag = 4 };
                var tid13 = new Tidsplaner { ReiseTid = "11:00", Ukedag = 4 };
                var tid14 = new Tidsplaner { ReiseTid = "15:00", Ukedag = 4 };
                var tid15 = new Tidsplaner { ReiseTid = "07:00", Ukedag = 5 };
                var tid16 = new Tidsplaner { ReiseTid = "11:00", Ukedag = 5 };
                var tid17 = new Tidsplaner { ReiseTid = "07:00", Ukedag = 6 };
                var tid18 = new Tidsplaner { ReiseTid = "12:00", Ukedag = 6 };
                var tid19 = new Tidsplaner { ReiseTid = "16:00", Ukedag = 6 };
                var tid20 = new Tidsplaner { ReiseTid = "08:00", Ukedag = 7 };
                var tid21 = new Tidsplaner { ReiseTid = "15:00", Ukedag = 7 };


                List<Rute> ruter = new List<Rute>();
                //Oslo
                ruter.Add(new Rute { StedNavn = steder[0], TId = tid1 });
                ruter.Add(new Rute { StedNavn = steder[0], TId = tid2 });
                ruter.Add(new Rute { StedNavn = steder[0], TId = tid3 });
                ruter.Add(new Rute { StedNavn = steder[0], TId = tid4 });
                ruter.Add(new Rute { StedNavn = steder[0], TId = tid5 });
                ruter.Add(new Rute { StedNavn = steder[0], TId = tid6 });
                ruter.Add(new Rute { StedNavn = steder[0], TId = tid7 });
                ruter.Add(new Rute { StedNavn = steder[0], TId = tid8 });
                ruter.Add(new Rute { StedNavn = steder[0], TId = tid9 });
                ruter.Add(new Rute { StedNavn = steder[0], TId = tid13 });
                ruter.Add(new Rute { StedNavn = steder[0], TId = tid14 });
                ruter.Add(new Rute { StedNavn = steder[0], TId = tid15 });
                ruter.Add(new Rute { StedNavn = steder[0], TId = tid16 });
                ruter.Add(new Rute { StedNavn = steder[0], TId = tid18 });
                ruter.Add(new Rute { StedNavn = steder[0], TId = tid20 });
                //Bergen
                ruter.Add(new Rute { StedNavn = steder[1], TId = tid1 });
                ruter.Add(new Rute { StedNavn = steder[1], TId = tid3 });
                ruter.Add(new Rute { StedNavn = steder[1], TId = tid6 });
                ruter.Add(new Rute { StedNavn = steder[1], TId = tid7 });
                ruter.Add(new Rute { StedNavn = steder[1], TId = tid8 });
                ruter.Add(new Rute { StedNavn = steder[1], TId = tid9 });
                ruter.Add(new Rute { StedNavn = steder[1], TId = tid13 });
                ruter.Add(new Rute { StedNavn = steder[1], TId = tid14 });
                ruter.Add(new Rute { StedNavn = steder[1], TId = tid15 });
                ruter.Add(new Rute { StedNavn = steder[1], TId = tid16 });
                ruter.Add(new Rute { StedNavn = steder[1], TId = tid19 });
                ruter.Add(new Rute { StedNavn = steder[1], TId = tid21 });
                //Stavanger
                ruter.Add(new Rute { StedNavn = steder[2], TId = tid1 });
                ruter.Add(new Rute { StedNavn = steder[2], TId = tid3 });
                ruter.Add(new Rute { StedNavn = steder[2], TId = tid6 });
                ruter.Add(new Rute { StedNavn = steder[2], TId = tid7 });
                ruter.Add(new Rute { StedNavn = steder[2], TId = tid8 });
                ruter.Add(new Rute { StedNavn = steder[2], TId = tid9 });
                ruter.Add(new Rute { StedNavn = steder[2], TId = tid13 });
                ruter.Add(new Rute { StedNavn = steder[2], TId = tid14 });
                ruter.Add(new Rute { StedNavn = steder[2], TId = tid15 });
                ruter.Add(new Rute { StedNavn = steder[2], TId = tid16 });
                ruter.Add(new Rute { StedNavn = steder[2], TId = tid18 });
                ruter.Add(new Rute { StedNavn = steder[2], TId = tid20 });
                //Kristiansand
                ruter.Add(new Rute { StedNavn = steder[3], TId = tid1 });
                ruter.Add(new Rute { StedNavn = steder[3], TId = tid2 });
                ruter.Add(new Rute { StedNavn = steder[3], TId = tid5 });
                ruter.Add(new Rute { StedNavn = steder[3], TId = tid7 });
                ruter.Add(new Rute { StedNavn = steder[3], TId = tid8 });
                ruter.Add(new Rute { StedNavn = steder[3], TId = tid11 });
                ruter.Add(new Rute { StedNavn = steder[3], TId = tid12 });
                ruter.Add(new Rute { StedNavn = steder[3], TId = tid13 });
                ruter.Add(new Rute { StedNavn = steder[3], TId = tid15 });
                ruter.Add(new Rute { StedNavn = steder[3], TId = tid17 });
                ruter.Add(new Rute { StedNavn = steder[3], TId = tid18 });
                ruter.Add(new Rute { StedNavn = steder[3], TId = tid20 });
                //Trondheim
                ruter.Add(new Rute { StedNavn = steder[4], TId = tid1 });
                ruter.Add(new Rute { StedNavn = steder[4], TId = tid6 });
                ruter.Add(new Rute { StedNavn = steder[4], TId = tid10 });
                ruter.Add(new Rute { StedNavn = steder[4], TId = tid13 });
                ruter.Add(new Rute { StedNavn = steder[4], TId = tid15 });
                ruter.Add(new Rute { StedNavn = steder[4], TId = tid18 });
                ruter.Add(new Rute { StedNavn = steder[4], TId = tid20 });
                foreach (Rute r in ruter) { context.Ruter.Add(r); }

                //Bestilling
                var kunde1 = new Kunder
                {
                    Epost = "perhansen@gmail.com",
                    Telefon = "22445678"
                };

                var bestilling1 = new Bestillingen
                {
                    BestillingsDato = DateTime.Now.Date,
                    Kunder = kunde1,
                    TotaltPris = 200
                };

                var billett1 = new EnkeltBillett
                {
                    Bestillinger = bestilling1,
                    BillettTyper = type1,
                    ReiseDato = new DateTime(2020, 10, 19),
                    Ruten = ruter[0],
                    Fra = steder[0],
                    Til = steder[1]
                };
                var billett2 = new EnkeltBillett
                {
                    Bestillinger = bestilling1,
                    BillettTyper = type1,
                    ReiseDato = new DateTime(2020, 10, 19),
                    Ruten = ruter[0],
                    Fra = steder[0],
                    Til = steder[1]
                };
                context.EnkeltBilletter.Add(billett1);
                context.EnkeltBilletter.Add(billett2);

                //Admin
                var bruker = new Brukere();
                bruker.Brukernavn = "Admin";
                string passord = "Admin123";
                byte[] salt = BussReiseRepository.LagSalt();
                byte[] hash = BussReiseRepository.LagHash(passord, salt);
                bruker.Passord = hash;
                bruker.Salt = salt;
                context.Brukere.Add(bruker);

                context.SaveChanges();
            }
        }
    }
}
