const purchaseButton = document.querySelector('.purchase');
const buttons = document.querySelector('.buttons');
const container = document.querySelector('.container');
const closeButton = document.querySelector('.close');
const inquiryButton = document.querySelector('inquiry');
const form = document.querySelector('form');
const input = document.querySelector('input');
const txt = document.getElementById('txt');
const remainTime = document.querySelector("#remain-time");
const images = document.querySelectorAll('.slider span');
const sliderContainer = document.querySelector('slider-container');
const slider = document.querySelector('.slider');
const prevBtn = document.querySelector('.leftBtn');
const nextBtn = document.querySelector('.rightBtn');
const tabItem = document.querySelectorAll('.tab_item')
const tabInner = document.querySelectorAll('.tab_inner')

// 슬라이더 페이지 번호 지정
function pageNumber__Init(){
    // 전채 배너 페이지 갯수 세팅해서 .slider 에 'data-slide-total' 넣기
    // var totalSlideNo = $('.main-bn > .slider > .slides > .bn').length;
    const totalSlideNo = document.querySelectorAll('.text-wrap');

    // $('.main-bn > .slider').attr('data-slide-total', totalSlideNo);
    slider.setAttribute('data-slide-total', totalSlideNo.length);

    // // 각 배너 페이지 번호 매기기
    // $('.main-bn > .slider > .slides > .bn').each(function(index, node){
    //     $(node).attr('data-slide-no', index + 1);
    // });
    totalSlideNo.forEach(function (index,node){
       index.setAttribute('data-slide-no', node);
    });

};

pageNumber__Init();


tabItem.forEach((tab, idx)=> {
    tab.addEventListener('click', function(){
        tabInner.forEach((inner)=> {
            inner.classList.remove('active')
        })

        tabItem.forEach((item)=> {
            item.classList.remove('active')
        })

        tabItem[idx].classList.add('active')
        tabInner[idx].classList.add('active')
    })
})


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
    const msg = input.value;

    if(msg){
        txt.textContent = msg;
        form.reset();
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






