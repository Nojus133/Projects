using Microsoft.EntityFrameworkCore;
using oblig3.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace oblig3.DAL
{
    public class FaqRepository : IFaqRepository
    {
        private readonly FaqContext _db;

        public FaqRepository(FaqContext db)
        {
            _db = db;
        }

        public async Task<List<Sporsmaal>> HentAlle()
        {
            try
            {
                List<Sporsmaal> alleFaq = await _db.Sporsmaalene.ToListAsync();
                return alleFaq;
            }
            catch
            {
                return null;
            }
        }

        public async Task<bool> Endre(Faq enSporsmaal)
        {
            try
            {
                Sporsmaal funnet = await _db.Sporsmaalene.FindAsync(enSporsmaal.Id);
                if (funnet != null)
                {
                    funnet.Rating = funnet.Rating + enSporsmaal.Rating;
                    await _db.SaveChangesAsync();
                    return true;
                }
                return false;
            }
            catch
            {
                return false;
            }
        }

        public async Task<bool> Lagre(Faq enFaq)
        {
            try
            {
                Kategori funnetKategori = await _db.Kategorier.SingleOrDefaultAsync(k => k.Kategorinavn == enFaq.Kategori);
                if (funnetKategori != null)
                {
                    Sporsmaal nyFaq = new Sporsmaal
                    {
                        Kategori = funnetKategori,
                        Spors = enFaq.Spors,
                        Rating = enFaq.Rating,
                        Svar = enFaq.Svar
                    };
                    _db.Sporsmaalene.Add(nyFaq);
                    await _db.SaveChangesAsync();
                    return true;
                }
                return false;
            }
            catch
            {
                return false;
            }
        }
    }
}
