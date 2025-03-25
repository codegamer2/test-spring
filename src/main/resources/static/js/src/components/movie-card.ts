import { Movie } from '../types/main';

export function createMovieCard(movie: Movie): string {
    return `
        <div class="col-md-3 col-sm-6 mb-4">
            <div class="card movie-card">
                <img src="${movie.posterPath}" 
                     class="card-img-top" 
                     alt="${movie.title}"
                     onerror="this.src='/images/no-poster.jpg'">
                <div class="card-body">
                    <h5 class="card-title">${movie.title}</h5>
                    <p class="card-text">
                        <small class="text-muted">${movie.releaseDate}</small>
                    </p>
                    <div class="d-flex justify-content-between align-items-center">
                        <div class="rating">
                            <i class="bi bi-star-fill"></i>
                            ${movie.rating.toFixed(1)}
                        </div>
                        <div class="genres">
                            ${movie.genres.slice(0, 2).map(genre => 
                                `<span class="badge bg-secondary me-1">${genre}</span>`
                            ).join('')}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;
}

export function createSkeletonMovieCard(): string {
    return `
        <div class="col-md-3 col-sm-6 mb-4">
            <div class="card movie-card">
                <div class="skeleton skeleton-movie-card"></div>
            </div>
        </div>
    `;
} 