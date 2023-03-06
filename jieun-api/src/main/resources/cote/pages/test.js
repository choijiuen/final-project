const purchaseButton = document.querySelector('.purchase');
const buttons = document.querySelector('.buttons');
const container = document.querySelector('.container');
const closeButton = document.querySelector('.close');
const inquiryButton = document.querySelector('inquiry');
const form = document.querySelector('form');
const input = document.querySelector('input');
const ul = document.querySelector('ul');
const remainTime = document.querySelector("#remain-time");

const images = document.querySelectorAll('.slider span');
const sliderContainer = document.querySelector('slider-container');
const slider = document.querySelector('.slider');
const prevBtn = document.querySelector('.leftBtn');
const nextBtn = document.querySelector('.rightBtn');

function pageNumber__Init(){
    // 전채 배너 페이지 갯수 세팅해서 .slider 에 'data-slide-total' 넣기
    const totalSlideNo = $('.slide > .slider-container > .slides > .bn').length;
    //console.log(totalSlideNo);

    $('.main-bn > .slider').attr('data-slide-total', totalSlideNo);

    // 각 배너 페이지 번호 매기기
    $('.main-bn > .slider > .slides > .bn').each(function(index, node){
        $(node).attr('data-slide-no', index + 1);
    });
};

pageNumber__Init();


let current = 1;
const imgSize = images[0].clientWidth;

slider.style.transform = `translateX(${-imgSize}px)`;

prevBtn.addEventListener('click',()=>{
    if( current <= 0) return;
    slider.style.transition = '400ms ease-in-out transform';
    current--;
    slider.style.transform = `translateX(${-imgSize * current}px)`;
})

nextBtn.addEventListener('click',()=>{
    if( current >= images.length -1 ) return;
    slider.style.transition = '400ms ease-in-out transform';
    current++;
    slider.style.transform = `translateX(${-imgSize * current}px)`;
})

slider.addEventListener('transitionend', ()=> {
    if(images[current].classList.contains('first-img')){
        slider.style.transition = 'none';
        current = images.length - 2;
        slider.style.transform = `translateX(${-imgSize * current}px)`;
    }
    if(images[current].classList.contains('last-img')){
        slider.style.transition = 'none';
        current = images.length - current;
        slider.style.transform = `translateX(${-imgSize * current}px)`;
    }
})


function diffDay(){
    const masTime = new Date("2023-12-25");
    const todayTime = new Date();
    const diff = masTime - todayTime;

    const diffDay = Math.floor(diff / (1000*60*60*24));
    const diffHour = Math.floor((diff / (1000*60*60)) % 24);
    const diffMain = Math.floor((diff / (1000*60)) % 60);
    const diffSec = Math.floor(diff / 1000 % 60);

    remainTime.innerText = `${diffDay}일 ${diffHour}시간 ${diffMain}분 ${diffSec}초`;
}

diffDay();
setInterval(diffDay, 1000);

form.addEventListener('submit', (event) =>{
    event.preventDefault();

    if(input.value !== ''){
        const li = document.createElement('li');
        li.innerText = input.value;
        ul.appendChild(li);

        input.value = '';
    }
});

purchaseButton.addEventListener('click', () => {
    container.style.display = 'flex';
    buttons.style.display = 'none';
});

closeButton.addEventListener('click', () =>{
    container.style.display = 'none';
    buttons.style.display = 'block';
});



// inquiryButton.addEventListener('click', () => {
//     container.style.display = 'flex';
//     buttons.style.display = 'none';
// });






