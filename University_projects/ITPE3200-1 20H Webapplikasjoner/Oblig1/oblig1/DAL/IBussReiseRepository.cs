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

        Task<int> LagreBillettene(Bestilling[] innBilletter);

        Task<List<EnkeltBillett>> HentBilletter(int id);
    }
}
