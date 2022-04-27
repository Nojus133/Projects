using oblig3.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace oblig3.DAL
{
    public interface IFaqRepository
    {
        Task<List<Sporsmaal>> HentAlle();

        Task<bool> Endre(Faq enSporsmaal);

        Task<bool> Lagre(Faq enFaq);
    }
}
