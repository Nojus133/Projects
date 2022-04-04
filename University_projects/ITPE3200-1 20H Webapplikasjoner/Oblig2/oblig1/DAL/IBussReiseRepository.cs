using oblig1.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace oblig1.DAL
{
    public interface IBussReiseRepository
    {
        Task<List<Strekninger>> HentSteder();

        Task<List<Rute>> FinnBillett(Bestilling b);

        Task<List<BillettType>> GetPris();

        Task<int?> LagreBillettene(Bestilling[] innBilletter);

        Task<List<EnkeltBillett>> HentBilletter(int id);

        Task<bool> LoggInn(Bruker bruker);

        Task<List<Strekninger>> HentStrekninger();

        Task<List<Kunder>> HentKunder();

        Task<List<Bestillingen>> HentBestillinger();

        Task<List<EnkeltBillett>> HentAlleBilletter();

        Task<List<Rute>> HentRuter();

        Task<List<Rute>> HentRutene(string id);

        Task<List<Tidsplaner>> HentTidsplaner();

        Task<bool> LagreType(BillettType type);

        Task<bool> LagreStrekning(Strekninger s);

        Task<bool> LagreTidsplan(Tidsplaner t);

        Task<bool> LagreRute(int id_sted, int id_tid);

        Task<bool> SlettType(int id);

        Task<bool> SlettStrekning(int id);

        Task<bool> SlettKunde(int id);

        Task<bool> SlettBillett(int id);

        Task<bool> SlettBestilling(int id);

        Task<bool> SlettRute(int id);

        Task<bool> SlettTidsplan(int id);

        Task<bool> EndreType(int id, BillettType obj);

        Task<bool> EndreStrekning(Strekninger obj);

        Task<bool> EndreKunde(Kunder obj);

        Task<bool> EndreTidsplan(Tidsplaner obj);

        Task<bool> EndreRute(int id, int id_sted, int id_tid);
    }
}
